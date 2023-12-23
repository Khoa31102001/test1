package tech.dut.fasto.web.rest.user.vnpay;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tech.dut.fasto.web.rest.user.vnpay.dto.request.VNPayCreateTokenPaymentUrlRequestDto;
import tech.dut.fasto.web.rest.user.vnpay.dto.request.VNPayCreateTokenRequestDto;
import tech.dut.fasto.web.rest.user.vnpay.dto.request.VNPayCreateUrlRequestDto;
import tech.dut.fasto.web.rest.user.vnpay.dto.response.UserCardResponseDto;
import tech.dut.fasto.web.rest.user.vnpay.dto.response.VNPayCreateUrlResponseDto;
import tech.dut.fasto.web.rest.user.vnpay.dto.response.VNPayReturnUrlResponseDto;
import tech.dut.fasto.web.rest.user.vnpay.dto.response.VNPayTokenResponseDto;
import tech.dut.fasto.web.rest.user.vnpay.service.VnPayUserService;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.List;
@RestController
@RequestMapping("/user/management/vn-pay")
@RequiredArgsConstructor
@Validated

public class VNPAYUserResource {
    private final VnPayUserService vnPayUserService;

    @ApiOperation("User payment with vn pay")
    @PostMapping(path = "/payment-url")
    public ResponseEntity<VNPayCreateUrlResponseDto> createPaymentUrl(@RequestBody VNPayCreateUrlRequestDto requestDTO, HttpServletRequest request) {
        return ResponseEntity.ok(vnPayUserService.createPaymentUrl(requestDTO, request));
    }

    @ApiOperation("Get return payment url")
    @GetMapping(path = "/return-url")
    public ResponseEntity<VNPayReturnUrlResponseDto> getReturnPaymentUrl(HttpServletRequest request) throws UnsupportedEncodingException {
        return ResponseEntity.ok(vnPayUserService.getReturnPaymentUrl(request));
    }

    @ApiOperation("Get ipn url")
    @GetMapping(path = "/ipn-url")
    public ResponseEntity<VNPayReturnUrlResponseDto> getIPNUrl(HttpServletRequest request) throws UnsupportedEncodingException {
        return ResponseEntity.ok(vnPayUserService.getIPNUrl(request));
    }


    @ApiOperation("User create token")
    @PostMapping("/token-url")
    public ResponseEntity<VNPayTokenResponseDto> createTokenUrl(@RequestBody VNPayCreateTokenRequestDto vnPayCreateTokenUrlRequestDTO, HttpServletRequest request) {
        return ResponseEntity.ok(vnPayUserService.createToken(request, vnPayCreateTokenUrlRequestDTO));
    }

    @ApiOperation("User payment with vnpay by token")
    @PostMapping("/token-pay")
    public ResponseEntity<VNPayCreateUrlResponseDto> createTokenPayment(@RequestBody VNPayCreateUrlRequestDto requestDTO, HttpServletRequest request) throws Exception {
        return ResponseEntity.ok(vnPayUserService.createTokenPay(requestDTO, request));
    }

    @ApiOperation("User payment with vnpay by token")
    @PostMapping("/token/payment")
    public ResponseEntity<VNPayCreateUrlResponseDto> createPaymentToken(@RequestBody VNPayCreateTokenPaymentUrlRequestDto requestDTO, HttpServletRequest request) throws Exception {
        return ResponseEntity.ok(vnPayUserService.createTokenPayment(request, requestDTO));
    }

    @ApiOperation("User remove token url")
    @DeleteMapping("/token/remove-url/{id}")
    public ResponseEntity<VNPayTokenResponseDto> deleteTokenUrl(@PathVariable Long id, HttpServletRequest request) {
        return ResponseEntity.ok(vnPayUserService.deleteToken(request, id));
    }

    @ApiOperation("User number cards")
    @GetMapping("/bank-cards")
    public ResponseEntity<List<UserCardResponseDto>> getAllNumberCards() {
        return ResponseEntity.ok(vnPayUserService.getAllListCards());
    }

}
