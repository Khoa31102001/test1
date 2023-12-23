package tech.dut.fasto.web.rest.admin.authentication;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.dut.fasto.common.dto.request.LoginRequestDto;
import tech.dut.fasto.common.dto.request.LogoutRequestDto;
import tech.dut.fasto.common.dto.response.JWTTokenResponseDto;
import tech.dut.fasto.common.service.UserJWTService;
import tech.dut.fasto.config.security.AuthoritiesConstants;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/authenticate")
@RequiredArgsConstructor
@Validated
public class AdminAuthenticationResource {
    private final UserJWTService userJWTService;

    @PostMapping
    public ResponseEntity<JWTTokenResponseDto> authorize(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        String jwt = userJWTService.authorize(loginRequestDto);
        JWTTokenResponseDto response = new JWTTokenResponseDto(jwt);
        userJWTService.validatedLogin(loginRequestDto.getEmail(), AuthoritiesConstants.ADMIN);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> userLogout(@RequestBody LogoutRequestDto logoutRequestDto) {
        userJWTService.logout(logoutRequestDto);
        return ResponseEntity.ok().body("Logout successfully");
    }

}
