package tech.dut.fasto.web.rest.user.authentication.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import io.swagger.models.auth.In;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import tech.dut.fasto.common.domain.*;
import tech.dut.fasto.common.domain.enumeration.Gender;
import tech.dut.fasto.common.domain.enumeration.Provider;
import tech.dut.fasto.common.domain.enumeration.UserStatus;
import tech.dut.fasto.common.dto.UserInformationDto;
import tech.dut.fasto.common.dto.request.CodeAuthenticateRequestDto;
import tech.dut.fasto.common.dto.request.EmailAuthenticateRequestDto;
import tech.dut.fasto.common.dto.response.JWTTokenResponseDto;
import tech.dut.fasto.common.repository.DeviceTokenInfoRepository;
import tech.dut.fasto.common.repository.UserInformationRepository;
import tech.dut.fasto.common.repository.UserRepository;
import tech.dut.fasto.common.service.EmailService;
import tech.dut.fasto.common.service.RoleService;
import tech.dut.fasto.common.service.impl.MessageService;
import tech.dut.fasto.config.security.AuthoritiesConstants;
import tech.dut.fasto.config.security.jwt.TokenProvider;
import tech.dut.fasto.config.security.oauth2.*;
import tech.dut.fasto.errors.FastoAlertException;
import tech.dut.fasto.util.constants.Constants;
import tech.dut.fasto.web.rest.user.authentication.dto.SignUpUserDto;
import tech.dut.fasto.web.rest.user.authentication.dto.request.LoginOauth2RequestDto;

import javax.validation.Valid;
import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserAuthenticationServiceImpl implements UserAuthenticationService {

    private final EmailService emailService;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final RoleService roleService;

    private final MessageService messageService;

    private final TokenProvider tokenProvider;

    private final RestTemplate restTemplate;

    private final DeviceTokenInfoRepository deviceTokenInfoRepository;



    @Override
    @Transactional(rollbackFor = FastoAlertException.class)
    public SignUpUserDto signUpUser(@Valid SignUpUserDto signUpUserDto) throws FastoAlertException {
        if (this.userRepository.findByEmail(signUpUserDto.getEmail()).isPresent()) {

            throw new FastoAlertException(messageService.getMessage("error.code.authenticate.register"), messageService.getMessage("error.authenticate.email.existed"));
        }
        if (this.userRepository.findByEmailAndStatus(signUpUserDto.getEmail(), UserStatus.ACTIVATED).isPresent()) {

            throw new FastoAlertException(messageService.getMessage("error.code.authenticate.register"), messageService.getMessage("error.authenticate.email.existed"));
        }
        if (null != signUpUserDto.getPhoneNumber() && this.userRepository.existsByPhoneNumberIgnoreCase(signUpUserDto.getPhoneNumber())) {
            throw new FastoAlertException(messageService.getMessage("error.code.authenticate.register"), messageService.getMessage("error.authenticate.phone.number.existed"));
        }
        if (this.checkPasswordInValid(signUpUserDto.getPassword())) {
            throw new FastoAlertException(messageService.getMessage("error.code.authenticate.register"), messageService.getMessage("error.authenticate.format.password.incorrect"));
        }
        User user = new User();
        user.setPassword(passwordEncoder.encode(signUpUserDto.getPassword()));
        user.setEmail(signUpUserDto.getEmail().toLowerCase());
        if (null != signUpUserDto.getPhoneNumber()) {
            user.setPhoneNumber(signUpUserDto.getPhoneNumber());
        }
        user.setStatus(UserStatus.INACTIVE);
        user.setProvider(Provider.LOCAL);

        createUserRoles(user, AuthoritiesConstants.USER);

        UserInformation userInformation = new UserInformation();
        userInformation.setUser(user);
        userInformation.setFirstName(signUpUserDto.getFirstname());
        if (null != signUpUserDto.getBirthDay()) {
            userInformation.setBirthday(Instant.ofEpochMilli(signUpUserDto.getBirthDay()).truncatedTo(ChronoUnit.DAYS));
        }

        user.setAuthCode(this.emailService.sendEmailForActiveUser(signUpUserDto.getEmail()));
        user.setExpiredTime(Instant.now().plus(1, ChronoUnit.DAYS));

        user.setUserInformation(userInformation);

        userRepository.save(user);
        signUpUserDto.setPassword(null);

        if (null != signUpUserDto.getDeviceToken()) {
            Optional<DeviceTokenInfo> optionalDeviceTokenInfo = deviceTokenInfoRepository.findByToken(signUpUserDto.getDeviceToken());
            if (optionalDeviceTokenInfo.isPresent()) {
                DeviceTokenInfo deviceTokenInfo = optionalDeviceTokenInfo.get();
                deviceTokenInfoRepository.delete(deviceTokenInfo);
            }
            DeviceTokenInfo deviceTokenInfo = new DeviceTokenInfo();
            deviceTokenInfo.setToken(signUpUserDto.getDeviceToken());
            deviceTokenInfo.setDisable(false);
            deviceTokenInfo.setUser(user);
        }

        return signUpUserDto;
    }

    @Override
    @Transactional(rollbackFor = FastoAlertException.class)
    public JWTTokenResponseDto activatedUser(CodeAuthenticateRequestDto codeAuthenticateRequestDto) {
        User user = userRepository.findByAuthCode(codeAuthenticateRequestDto.getCode()).orElseThrow(
                () -> new FastoAlertException(messageService.getMessage("error.code.authenticate.active.account"), messageService.getMessage("error.jwt.token.incorrect"))
        );
        if (Instant.now().isAfter(user.getExpiredTime())) {
            throw new FastoAlertException(messageService.getMessage("error.code.authenticate.active.account"), messageService.getMessage("error.jwt.token.expired"));
        }
        user.setStatus(UserStatus.ACTIVATED);
        user.setAuthCode(null);
        user.setAuthCode(null);

        return this.createTokenFromUser(user);

    }

    private boolean checkPasswordInValid(String password) {
        return !password.matches(Constants.PASSWORD_PATTERN);
    }

    @Override
    @Transactional
    public JWTTokenResponseDto signInWithGoogleApp(@Valid LoginOauth2RequestDto dto) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, dto.getTokenType() + " " + dto.getAccessToken());
        HttpEntity entity = new HttpEntity(headers);
        GoogleAccountDTO googleAccountDTO = restTemplate.exchange(Constants.GOOGLE_INFO_URI, HttpMethod.GET, entity, GoogleAccountDTO.class).getBody();
        Optional<User> optionalUser = userRepository.findFirstByOauthIdAndProvider(googleAccountDTO.getId(), Provider.GOOGLE);

        GoogleAccountInfoDto googleAccountInfoDto = restTemplate.exchange(Constants.GOOGLE_INFO_URI_BIRTHDAY_GENDER + dto.getAccessToken(), HttpMethod.GET, null, GoogleAccountInfoDto.class).getBody();
        ZonedDateTime zdt = Instant.now().atZone(ZoneOffset.UTC);
        Instant birthday = Instant.now();
        if(googleAccountInfoDto.getBirthdays()!=null && googleAccountInfoDto.getBirthdays()[0]!=null){
            GoogleDate googleDate = googleAccountInfoDto.getBirthdays()[0].getDate();
             birthday = zdt.withDayOfMonth(googleDate.getDay()).withMonth(googleDate.getMonth()).withYear(googleDate.getYear()).toInstant().truncatedTo(ChronoUnit.HOURS);
        }

        String gender = null;
        if(googleAccountInfoDto.getGenders()!=null && googleAccountInfoDto.getGenders()[0]!=null){
            gender  = googleAccountInfoDto.getGenders()[0].getValue();
        }
        User user;
        if (optionalUser.isEmpty()) {
            user = createUserByGoogle(googleAccountDTO, birthday, gender);
        } else {
            user = optionalUser.get();
        }
        if (null != dto.getDeviceToken()) {
            handleSaveDeviceToken(user, dto.getDeviceToken());
        }
        return this.createTokenFromUser(user);
    }

    @Override
    @Transactional
    public JWTTokenResponseDto signInWithFacebookApp(LoginOauth2RequestDto dto) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json;charset=utf-8");
        HttpEntity<String> entity = new HttpEntity<>(headers);
        String facebookGetInfoUrl = Constants.FACEBOOK_INFO_URI + "&access_token=" + dto.getAccessToken();
        FacebookAccountDTO facebookAccountDTO = restTemplate.exchange(facebookGetInfoUrl, HttpMethod.GET, entity, FacebookAccountDTO.class).getBody();
        Optional<User> oUser = this.userRepository.findFirstByOauthIdAndProvider(facebookAccountDTO.getId(), Provider.FACEBOOK);
        User user;
        if (oUser.isEmpty()) {
            user = createUserByFacebook(facebookAccountDTO);
        } else {
            user = oUser.get();
        }
        if (null != dto.getDeviceToken()) {
            handleSaveDeviceToken(user, dto.getDeviceToken());
        }
        return this.createTokenFromUser(user);
    }

    @Override
    @Transactional(rollbackFor = FastoAlertException.class)
    public void resendDigitCode(EmailAuthenticateRequestDto emailAuthenticateRequestDto) {
        User user = userRepository.findFirstByEmail(emailAuthenticateRequestDto.getEmail()).orElseThrow(
                () -> new FastoAlertException(messageService.getMessage("error.code.authenticate.resend.code"), messageService.getMessage("error.authenticate.user.not.existed")));

        if (UserStatus.ACTIVATED.equals(user.getStatus())) {
            throw new FastoAlertException(messageService.getMessage("error.code.authenticate.resend.code"), messageService.getMessage("error.authenticate.user.already.activated"));
        }

        this.emailService.sendEmailForActiveUser(emailAuthenticateRequestDto.getEmail());

    }



    @Transactional
    public User createUserByGoogle(GoogleAccountDTO googleAccountDTO, Instant birthDate, String gender) {
        User user = new User();
        user.setStatus(UserStatus.ACTIVATED);
        user.setProvider(Provider.GOOGLE);
        user.setEmail(googleAccountDTO.getEmail());
        user.setOauthId(googleAccountDTO.getId());

        createUserRoles(user, AuthoritiesConstants.USER);

        UserInformation userInformation = new UserInformation();
        userInformation.setUser(user);
        userInformation.setFirstName(googleAccountDTO.getFamily_name());
        userInformation.setLastName(googleAccountDTO.getGiven_name());
        userInformation.setUserImage(googleAccountDTO.getPicture());
        userInformation.setBirthday(birthDate);
        userInformation.setGender(getGenderUser(gender));

        user.setUserInformation(userInformation);
        userRepository.save(user);
        return user;
    }

    @Transactional
    public User createUserByFacebook(FacebookAccountDTO facebookAccountDTO) {
        User user = new User();
        user.setStatus(UserStatus.ACTIVATED);
        user.setProvider(Provider.FACEBOOK);
        user.setEmail(facebookAccountDTO.getEmail());
        user.setOauthId(facebookAccountDTO.getId());
        createUserRoles(user, AuthoritiesConstants.USER);

        UserInformation userInformation = new UserInformation();
        userInformation.setUser(user);
        userInformation.setFirstName(facebookAccountDTO.getFirstName());
        userInformation.setLastName(facebookAccountDTO.getLastName());
        userInformation.setGender(getGenderUser(facebookAccountDTO.getGender()));
         final DateTimeFormatter FMT = new DateTimeFormatterBuilder()
                .appendPattern("MM/dd/yyyy")
                .parseDefaulting(ChronoField.NANO_OF_DAY, 0)
                .toFormatter()
                .withZone(ZoneId.of("Asia/Ho_Chi_Minh"));
        Instant birthday = FMT.parse(facebookAccountDTO.getBirthday(), Instant::from);
        userInformation.setBirthday(birthday);
        Map<String, Object> pictureObj = facebookAccountDTO.getPicture();
        if (pictureObj.containsKey("data")) {
            Map<String, Object> dataObj = (Map<String, Object>) pictureObj.get("data");
            if (dataObj.containsKey("url")) {
                userInformation.setUserImage((String) dataObj.get("url"));
            }
        }
        user.setUserInformation(userInformation);
        userRepository.save(user);
        return user;
    }

    private Gender getGenderUser(String gender){
        if (null == gender){
            return Gender.UNKNOWN;

        }
        if(Gender.MALE.toString().equals(gender.toUpperCase())){
            return Gender.MALE;
        }
        if(Gender.FEMALE.toString().equals(gender.toUpperCase())){
            return Gender.FEMALE;
        }
        return Gender.UNKNOWN;
    }
    @Transactional
    public void handleSaveDeviceToken(User user, String token) {
        Optional<DeviceTokenInfo> deviceTokenInfoOptional = deviceTokenInfoRepository.findByTokenAndUserId(token, user.getId());
        if (deviceTokenInfoOptional.isPresent()) {
            DeviceTokenInfo deviceTokenInfo = deviceTokenInfoOptional.get();
            deviceTokenInfo.setDisable(false);
            deviceTokenInfo.setToken(token);
        } else {
            DeviceTokenInfo deviceTokenInfo = new DeviceTokenInfo();
            deviceTokenInfo.setDisable(false);
            deviceTokenInfo.setToken(token);
            deviceTokenInfo.setUser(user);
            deviceTokenInfoRepository.save(deviceTokenInfo);
        }
    }


    private JWTTokenResponseDto createTokenFromUser(User user) {
        Authentication authentication = tokenProvider.getAuthenticationFromUser(user);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenProvider.createToken(authentication, false);
        return new JWTTokenResponseDto(token);
    }

    private void createUserRoles(User user, String... roleNames) {
        Set<UserRole> userRoles = new HashSet<>();

        for (String roleName : roleNames) {
            Role role = roleService.findOneByName(roleName);
            UserRole userRole = new UserRole();
            userRole.setRole(role);
            userRole.setUser(user);
            userRoles.add(userRole);
        }

        user.setUserRoles(userRoles);
    }


}
