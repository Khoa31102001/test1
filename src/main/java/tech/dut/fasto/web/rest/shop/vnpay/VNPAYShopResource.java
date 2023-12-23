package tech.dut.fasto.web.rest.shop.vnpay;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.dut.fasto.web.rest.shop.vnpay.dto.request.VNPayCreateUrlRequestShopDto;
import tech.dut.fasto.web.rest.shop.vnpay.dto.response.VNPayCreateUrlResponseShopDto;
import tech.dut.fasto.web.rest.shop.vnpay.service.VnPayShopService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/shop/management/vn-pay")
@RequiredArgsConstructor
@Validated

public class VNPAYShopResource {
    private final VnPayShopService vnPayShopService;

    @ApiOperation("User payment with vn pay")
    @PostMapping(path = "/payment-url")
    public ResponseEntity<VNPayCreateUrlResponseShopDto> createPaymentUrl(@RequestBody VNPayCreateUrlRequestShopDto requestDTO, HttpServletRequest request) {
        return ResponseEntity.ok(vnPayShopService.depositForSystem(requestDTO, request));
    }

}
