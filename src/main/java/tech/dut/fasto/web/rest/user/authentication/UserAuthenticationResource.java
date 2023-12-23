package tech.dut.fasto.web.rest.user.authentication;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tech.dut.fasto.common.dto.request.*;
import tech.dut.fasto.common.dto.response.JWTTokenResponseDto;
import tech.dut.fasto.common.service.UserJWTService;
import tech.dut.fasto.config.security.AuthoritiesConstants;
import tech.dut.fasto.web.rest.user.authentication.dto.SignUpUserDto;
import tech.dut.fasto.web.rest.user.authentication.dto.request.LoginOauth2RequestDto;
import tech.dut.fasto.web.rest.user.authentication.service.UserAuthenticationService;


import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/user/authenticate")
@RequiredArgsConstructor
@Validated
@Api(tags = "User Authentication")
public class UserAuthenticationResource {
    private final UserAuthenticationService userAuthenticationService;

    private final UserJWTService userJWTService;


    @PostMapping("/forgot-password")
    public ResponseEntity<String> userForgotPassword(@Valid @RequestBody EmailAuthenticateRequestDto emailAuthenticateRequestDto) {
        userJWTService.forgotPassword(emailAuthenticateRequestDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/forgot-password/verify-code")
    public ResponseEntity<String> verifyForgotPasswordDigitCode(@Valid @RequestBody CodeAuthenticateRequestDto codeAuthenticateRequestDto) {
        return ResponseEntity.ok().body(userJWTService.verifyForgotPasswordDigitCode(codeAuthenticateRequestDto));
    }

    @PostMapping("/forgot-password/reset")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPasswordRequestDto resetPasswordRequestDto) {
        userJWTService.resetPassword(resetPasswordRequestDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/register")
    public ResponseEntity<SignUpUserDto> registerUser(@Valid @RequestBody SignUpUserDto signUpUserDto) {
        SignUpUserDto response = userAuthenticationService.signUpUser(signUpUserDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/activate-code")
    public ResponseEntity<JWTTokenResponseDto> activateCode(@Valid @RequestBody CodeAuthenticateRequestDto codeAuthenticateRequestDto) {
        JWTTokenResponseDto response = userAuthenticationService.activatedUser(codeAuthenticateRequestDto);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/google")
    public ResponseEntity<JWTTokenResponseDto> signInWithGoogle(@Valid @RequestBody LoginOauth2RequestDto loginOauth2RequestDto) throws IOException {
        JWTTokenResponseDto response = userAuthenticationService.signInWithGoogleApp(loginOauth2RequestDto);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/facebook")
    public ResponseEntity<JWTTokenResponseDto> signInWithFacebook(@Valid @RequestBody LoginOauth2RequestDto loginOauth2RequestDto) {
        JWTTokenResponseDto response = userAuthenticationService.signInWithFacebookApp(loginOauth2RequestDto);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/re-send-code")
    public ResponseEntity<String> resendDigitCode(@Valid @RequestBody EmailAuthenticateRequestDto emailAuthenticateRequestDto) {
        userAuthenticationService.resendDigitCode(emailAuthenticateRequestDto);
        return ResponseEntity.ok().body("Resend code successfully");
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@Valid @RequestBody ChangePasswordRequestDto changePasswordRequestDto) {

        userJWTService.changePassword(changePasswordRequestDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<JWTTokenResponseDto> authorize(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        String jwt = userJWTService.authorize(loginRequestDto);
        JWTTokenResponseDto response = new JWTTokenResponseDto(jwt);
        userJWTService.validatedLogin(loginRequestDto.getEmail(), AuthoritiesConstants.USER);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> userLogout(@RequestBody LogoutRequestDto logoutRequestDto) {
        userJWTService.logout(logoutRequestDto);
        return ResponseEntity.ok().body("Logout successfully");
    }

    @GetMapping("/profile/{id}")
    public ResponseEntity<String> userLogout(@PathVariable("id") Long userId) {

        return ResponseEntity.ok().body("Logout successfully");
    }

}
