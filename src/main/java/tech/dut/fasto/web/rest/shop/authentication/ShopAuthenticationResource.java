package tech.dut.fasto.web.rest.shop.authentication;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tech.dut.fasto.common.dto.request.*;
import tech.dut.fasto.common.dto.response.JWTTokenResponseDto;
import tech.dut.fasto.common.service.UserJWTService;
import tech.dut.fasto.config.security.AuthoritiesConstants;
import tech.dut.fasto.web.rest.shop.authentication.dto.DepositShopDto;
import tech.dut.fasto.web.rest.shop.authentication.dto.SignUpShopDto;
import tech.dut.fasto.web.rest.shop.authentication.service.ShopAuthenticateService;

import javax.validation.Valid;

@RestController
@RequestMapping("/shop/authenticate")
@RequiredArgsConstructor
@Validated
@Api(tags = "Shop Authentication")
public class ShopAuthenticationResource {

    private final ShopAuthenticateService shopAuthenticateService;

    private final UserJWTService userJWTService;

    @PostMapping
    public ResponseEntity<JWTTokenResponseDto> authorize(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        String jwt = userJWTService.authorize(loginRequestDto);
        JWTTokenResponseDto response = new JWTTokenResponseDto(jwt);
        userJWTService.validatedLogin(loginRequestDto.getEmail(), AuthoritiesConstants.SHOP);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> userLogout(@RequestBody LogoutRequestDto logoutRequestDto) {
        userJWTService.logout(logoutRequestDto);
        return ResponseEntity.ok().body("Logout successfully");
    }

    @PostMapping("/register")
    public ResponseEntity<SignUpShopDto> signUpUser(@Valid @RequestBody SignUpShopDto signUpShopDto) {
        return ResponseEntity.ok().body(shopAuthenticateService.register(signUpShopDto));
    }

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

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@Valid @RequestBody ChangePasswordRequestDto changePasswordRequestDto) {

        userJWTService.changePassword(changePasswordRequestDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/is-deposit")
    public ResponseEntity<DepositShopDto> validateDeposit() {
        return ResponseEntity.ok(shopAuthenticateService.validateDeposit());
    }
}
