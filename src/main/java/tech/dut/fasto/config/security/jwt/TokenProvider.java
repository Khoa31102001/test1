package tech.dut.fasto.config.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.TextCodec;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import tech.dut.fasto.common.domain.Role;
import tech.dut.fasto.common.domain.UserRole;
import tech.dut.fasto.config.properties.FastoProperties;
import tech.dut.fasto.errors.JWTAlertException;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TokenProvider {
    private final Logger logger = LoggerFactory.getLogger(TokenProvider.class);

    private static final String AUTHORITIES_KEY = "auth";

    private static final String INVALID_JWT_TOKEN = "Invalid JWT token.";

    private static final String DEFAULT_SECRET = "fasto";

    private final FastoProperties fastoProperties;

    public String createToken(Authentication authentication, boolean rememberMe) {
        String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));
        long now = (new Date()).getTime();
        Date validity;
        if (rememberMe) {
            validity = new Date(now + 1000 * fastoProperties.getSecurity().getAuthentication().getJwt().getTokenValidityInSeconds());
        } else {
            validity = new Date(now + 1000 * fastoProperties.getSecurity().getAuthentication().getJwt().getTokenValidityInSecondsForRememberMe());
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put(AUTHORITIES_KEY, authorities);
        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(authentication.getName())
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS512, getKeySecret())
                .compact();
    }

    private String getKeySecret() {
        String secretKey;
        if (StringUtils.isEmpty(fastoProperties.getSecurity().getAuthentication().getJwt().getSecret())) {
            logger.warn("Warning: the JWT key used is not Base64-encoded. " +
                    "We recommend using the `jhipster.security.authentication.jwt.base64-secret` key for optimum security.");
            secretKey = DEFAULT_SECRET;
        } else {
            logger.debug("Using a Base64-encoded JWT secret key");
            secretKey = TextCodec.BASE64.encode(fastoProperties.getSecurity().getAuthentication().getJwt().getSecret());
        }
        return secretKey;
    }

    private Claims getClaims(String token) {
        return Jwts.parser().setSigningKey(getKeySecret()).parseClaimsJws(token).getBody();
    }

    public String getSubject(String token) {
        return getClaims(token).getSubject();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);

        Collection<? extends GrantedAuthority> authorities = Arrays
                .stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                .filter(auth -> !auth.trim().isEmpty())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public Authentication getAuthenticationFromUser(tech.dut.fasto.common.domain.User user) {
        Collection<? extends GrantedAuthority> authorities =  user.getUserRoles().stream().map(UserRole::getRole).map(Role::getName)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        User principal = new User(user.getEmail(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public boolean validateToken(String authToken) throws JWTAlertException {
        try {
            getClaims(authToken);
            return true;
        } catch (ExpiredJwtException e) {
            logger.trace(INVALID_JWT_TOKEN, e);
            throw new JWTAlertException("token expired");
        } catch (UnsupportedJwtException e) {
            logger.trace(INVALID_JWT_TOKEN, e);
            throw new JWTAlertException("token unsupported");
        } catch (MalformedJwtException e) {
            logger.trace(INVALID_JWT_TOKEN, e);
            throw new JWTAlertException("token malformed");
        } catch (SignatureException e) {
            logger.trace(INVALID_JWT_TOKEN, e);
            throw new JWTAlertException("token invalid-signature");
        }
    }

    public String createVNPayToken(String token) {
        return Jwts.builder().setSubject(token)
                .signWith(SignatureAlgorithm.HS512, fastoProperties.getSecurity().getAuthentication().getJwt().getSecret())
                .compact();
    }

    public String getVNPayTokenJwtToken(String token) {
        return Jwts.parser().setSigningKey(fastoProperties.getSecurity().getAuthentication().getJwt().getSecret()).parseClaimsJws(token).getBody().getSubject();
    }

}
