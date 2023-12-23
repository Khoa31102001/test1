package tech.dut.fasto.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;
import tech.dut.fasto.config.security.jwt.JWTConfigurer;
import tech.dut.fasto.config.security.jwt.TokenProvider;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Import(SecurityProblemSupport.class)
@RequiredArgsConstructor
public class SecurityConfiguration {


    private final CorsFilter corsFilter;

    private final TokenProvider tokenProvider;

    private final SecurityProblemSupport problemSupport;

    private final UserDetailsService userDetailsService;

    private final HandlerExceptionResolver exceptionResolver;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfiguration) throws Exception {
        return authConfiguration.getAuthenticationManager();
    }

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(problemSupport)
                .accessDeniedHandler(problemSupport)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider())
                .authorizeRequests().antMatchers(URL_PERMIT_ALL).permitAll()
                .anyRequest().authenticated()
                .and()
                .httpBasic()
                .and()
                .apply(securityConfigurerAdapter());
        return http.build();
    }

    private JWTConfigurer securityConfigurerAdapter() {
        return new JWTConfigurer(tokenProvider, exceptionResolver);
    }

    private static final String[] URL_PERMIT_ALL = {
            "/**/authenticate/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/webjars/**",
            "/**/swagger-ui.html",
            "/api/v2/docs",
            "/**/swagger-ui.html",
            "/user/management/product/**",
            "/user/management/shop/detail/**",
            "/user/management/shop/distance",
            "/user/management/shop/distance-shop",
            "/user/management/shop/shops",
            "/user/management/shop/top-shops",
            "/user/management/vouchers/{id}",
            "/user/management/vouchers",
            "/user/management/rating/**",
            "/user/management/vn-pay/ipn-url",
            "/user/management/vn-pay/token-url",
            "/admin/authenticate/notification/topic",
            "/user/management/carts",
            "/user/management/all-carts",
            "/user/management/community/reviews",
            "/user/management/community/top-reviews",
            "/user/management/community/review-images/{reviewId}",
            "/user/management/community/replies",
            "/user/management/community/top-replies",
            "/user/management/community/re-replies",
            "/user/management/community/replies",
            "/user/management/community/top-re-replies",
    };
}
