package tech.dut.fasto.web.rest.shop.vnpay.service;

import tech.dut.fasto.web.rest.shop.vnpay.dto.request.VNPayCreateUrlRequestShopDto;
import tech.dut.fasto.web.rest.shop.vnpay.dto.response.VNPayCreateUrlResponseShopDto;
import tech.dut.fasto.web.rest.user.vnpay.dto.request.VNPayCreateUrlRequestDto;
import tech.dut.fasto.web.rest.user.vnpay.dto.response.VNPayCreateUrlResponseDto;

import javax.servlet.http.HttpServletRequest;

public interface VnPayShopService {
    VNPayCreateUrlResponseShopDto depositForSystem(VNPayCreateUrlRequestShopDto requestDTO, HttpServletRequest request);
}
