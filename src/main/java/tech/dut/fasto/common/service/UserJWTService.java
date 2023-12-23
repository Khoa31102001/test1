package tech.dut.fasto.common.service;

import tech.dut.fasto.common.dto.request.*;


public interface UserJWTService {

    String authorize(LoginRequestDto loginRequestDto);

    void logout(LogoutRequestDto logoutRequestDto);

    void forgotPassword(EmailAuthenticateRequestDto emailAuthenticateRequestDto);

    String verifyForgotPasswordDigitCode(CodeAuthenticateRequestDto codeAuthenticateRequestDto);

    void resetPassword(ResetPasswordRequestDto resetPasswordRequestDto);

    void changePassword(ChangePasswordRequestDto changePasswordRequestDto);
    void validatedLogin(String email, String role);


}
