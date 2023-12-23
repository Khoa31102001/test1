package tech.dut.fasto.web.rest.user.authentication.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import tech.dut.fasto.common.dto.UserInformationDto;
import tech.dut.fasto.common.dto.request.CodeAuthenticateRequestDto;
import tech.dut.fasto.common.dto.request.EmailAuthenticateRequestDto;
import tech.dut.fasto.common.dto.response.JWTTokenResponseDto;
import tech.dut.fasto.web.rest.user.authentication.dto.SignUpUserDto;
import tech.dut.fasto.web.rest.user.authentication.dto.request.LoginOauth2RequestDto;

import java.io.IOException;

public interface UserAuthenticationService {
    SignUpUserDto signUpUser(SignUpUserDto signUpUserDto);

    JWTTokenResponseDto activatedUser(CodeAuthenticateRequestDto codeAuthenticateRequestDto);

    JWTTokenResponseDto signInWithGoogleApp(LoginOauth2RequestDto dto) throws IOException;

    JWTTokenResponseDto signInWithFacebookApp(LoginOauth2RequestDto dto);

    void resendDigitCode(EmailAuthenticateRequestDto emailAuthenticateRequestDto);

}
