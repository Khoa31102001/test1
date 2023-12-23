package tech.dut.fasto.common.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.dut.fasto.common.domain.DeviceTokenInfo;
import tech.dut.fasto.common.domain.User;
import tech.dut.fasto.common.domain.enumeration.Provider;
import tech.dut.fasto.common.domain.enumeration.UserStatus;
import tech.dut.fasto.common.dto.request.*;
import tech.dut.fasto.common.repository.DeviceTokenInfoRepository;
import tech.dut.fasto.common.repository.UserInformationRepository;
import tech.dut.fasto.common.repository.UserRepository;
import tech.dut.fasto.common.repository.UserRoleRepository;
import tech.dut.fasto.common.service.EmailService;
import tech.dut.fasto.common.service.UserJWTService;
import tech.dut.fasto.config.security.SecurityUtils;
import tech.dut.fasto.config.security.jwt.TokenProvider;
import tech.dut.fasto.errors.FastoAlertException;
import tech.dut.fasto.util.constants.Constants;


import javax.validation.Valid;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserJWTServiceImpl implements UserJWTService {

    private final TokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final EmailService emailService;

    private final UserRoleRepository userRoleRepository;

    private final PasswordEncoder passwordEncoder;

    private final MessageService messageService;

    private final DeviceTokenInfoRepository deviceTokenInfoRepository;

    private final UserInformationRepository userInformationRepository;

    @Override
    @Transactional
    public String authorize(@Valid LoginRequestDto loginRequestDto) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginRequestDto.getEmail(),
                loginRequestDto.getPassword()
        );

        Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        if(null != loginRequestDto.getDeviceToken()){
            User user = userRepository.findByEmail(loginRequestDto.getEmail()).orElseThrow(() -> new FastoAlertException(this.messageService.getMessage("error.code.advice.authenticate.failed"), this.messageService.getMessage("error.authenticate.email.incorrect")));
            handleSaveDeviceToken(user,loginRequestDto.getDeviceToken());
        }
        return tokenProvider.createToken(authentication, loginRequestDto.isRememberMe());
    }

    @Override
    @Transactional
    public void logout(LogoutRequestDto logoutRequestDto) {
        String email = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.authenticate.logout"), messageService.getMessage("error.authenticate.unauthorized.user")));
        User user = userRepository.findByEmailAndStatus(email, UserStatus.ACTIVATED).orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.authenticate.logout"), messageService.getMessage("error.authenticate.user.already.activated")));

        DeviceTokenInfo deviceTokenInfo = deviceTokenInfoRepository.findByTokenAndUserId(logoutRequestDto.getDeviceToken(),user.getId()).orElseThrow(() -> new FastoAlertException(this.messageService.getMessage("error.code.authenticate.logout"),this.messageService.getMessage("error.authenticate.logout.failed.device.token.not.found")) );
        deviceTokenInfo.setDisable(true);
        SecurityContextHolder.getContext().setAuthentication(null);
    }

    @Override
    @Transactional(rollbackFor = FastoAlertException.class)
    public void forgotPassword(EmailAuthenticateRequestDto emailAuthenticateRequestDto) {
        User user = userRepository.findByEmailAndProvider(emailAuthenticateRequestDto.getEmail(), Provider.LOCAL).orElseThrow(() -> new FastoAlertException(this.messageService.getMessage("error.code.authenticate.reset.password.failed"), this.messageService.getMessage("error.authenticate.email.incorrect")));

        String digitCode = this.emailService.sendEmailForForgotPassword(emailAuthenticateRequestDto.getEmail());
        user.setExpiredTime(Instant.now().plus(1, ChronoUnit.DAYS));
        user.setAuthCode(digitCode);
    }

    @Override
    @Transactional(rollbackFor = FastoAlertException.class)
    public String verifyForgotPasswordDigitCode(CodeAuthenticateRequestDto codeAuthenticateRequestDto) {
        String codeMessage = this.messageService.getMessage("error.code.jwt.verified.code.failed");

        User user = userRepository.findByAuthCode(codeAuthenticateRequestDto.getCode()).orElseThrow(
                () -> new FastoAlertException(codeMessage, this.messageService.getMessage("error.jwt.token.incorrect")));
        if (Instant.now().isAfter(user.getExpiredTime())) {
            throw new FastoAlertException(codeMessage, this.messageService.getMessage("error.jwt.token.expired"));
        }
        Authentication authentication = this.tokenProvider.getAuthenticationFromUser(user);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return tokenProvider.createToken(authentication, false);
    }

    @Override
    @Transactional(rollbackFor = FastoAlertException.class)
    public void resetPassword(ResetPasswordRequestDto resetPasswordRequestDto) {

        if (!resetPasswordRequestDto.getNewPassword().equals(resetPasswordRequestDto.getConfirmNewPassword())) {
            throw new FastoAlertException(this.messageService.getMessage("error.code.authenticate.reset.password.failed"), this.messageService.getMessage("error.authenticate.confirm.new.password.incorrect"));
        }

        if (checkPasswordInValid(resetPasswordRequestDto.getNewPassword())) {
            throw new FastoAlertException(this.messageService.getMessage("error.code.authenticate.reset.password.failed"),this.messageService.getMessage("error.authenticate.format.password.incorrect"));
        }

        String email = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new FastoAlertException(this.messageService.getMessage("error.code.authenticate.reset.password.failed"), this.messageService.getMessage("error.bad.request")));
        User user = userRepository.findByEmailAndProvider(email, Provider.LOCAL).orElseThrow(
                () -> new FastoAlertException(this.messageService.getMessage("error.code.authenticate.reset.password.failed"), this.messageService.getMessage("error.jwt.token.incorrect"))
        );

        user.setPassword(passwordEncoder.encode(resetPasswordRequestDto.getNewPassword()));
        user.setExpiredTime(null);
        user.setAuthCode(null);
    }

    @Override
    @Transactional(rollbackFor = FastoAlertException.class)
    public void changePassword(ChangePasswordRequestDto changePasswordRequestDto) {
        if (!changePasswordRequestDto.getConfirmNewPassword().equals(changePasswordRequestDto.getNewPassword())) {
            throw new FastoAlertException(this.messageService.getMessage("error.code.authenticate.change.password.failed"), this.messageService.getMessage("error.authenticate.confirm.new.password.incorrect"));
        }

        if (checkPasswordInValid(changePasswordRequestDto.getNewPassword())) {
            throw new FastoAlertException(this.messageService.getMessage("error.code.authenticate.change.password.failed"), this.messageService.getMessage("error.authenticate.format.password.incorrect"));
        }

        String email = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new FastoAlertException(this.messageService.getMessage("error.code.authenticate.change.password.failed"), this.messageService.getMessage("error.authenticate.unauthorized.user")));

        User user = userRepository.findByEmailAndProvider(email, Provider.LOCAL).orElseThrow(
                () -> new FastoAlertException(this.messageService.getMessage("error.code.authenticate.change.password.failed"), this.messageService.getMessage("error.jwt.token.incorrect"))
        );
        if (!passwordEncoder.matches(changePasswordRequestDto.getCurrentPassword(), user.getPassword())) {
            throw new FastoAlertException(this.messageService.getMessage("error.code.authenticate.change.password.failed"), this.messageService.getMessage("error.authenticate.current.password.incorrect"));
        }
        user.setPassword(passwordEncoder.encode(changePasswordRequestDto.getNewPassword()));
    }

    @Override
    public void validatedLogin(String email, String role) throws FastoAlertException {
        if (userRoleRepository.findByUserEmailAndRoleName(email, role).isEmpty()) {
            throw new FastoAlertException(this.messageService.getMessage("error.code.authenticate.Login.failed"), this.messageService.getMessage("error.authenticate.email.invalid"));
        }
    }

    private boolean checkPasswordInValid(String password) {
        return !password.matches(Constants.PASSWORD_PATTERN);
    }

    @Transactional
    public void handleSaveDeviceToken(User user, String token){
        Optional<DeviceTokenInfo> deviceTokenInfoOptional = deviceTokenInfoRepository.findByTokenAndUserId(token, user.getId());
        if(deviceTokenInfoOptional.isPresent()){
            DeviceTokenInfo deviceTokenInfo = deviceTokenInfoOptional.get();
            deviceTokenInfo.setDisable(false);
            deviceTokenInfo.setToken(token);

        }else{
            DeviceTokenInfo deviceTokenInfo = new DeviceTokenInfo();
            deviceTokenInfo.setDisable(false);
            deviceTokenInfo.setToken(token);
            deviceTokenInfo.setUser(user);
            deviceTokenInfoRepository.save(deviceTokenInfo);
        }
    }

}
