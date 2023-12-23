package tech.dut.fasto.web.rest.user.vnpay.service;

import tech.dut.fasto.web.rest.user.vnpay.dto.request.VNPayCreateTokenPaymentUrlRequestDto;
import tech.dut.fasto.web.rest.user.vnpay.dto.request.VNPayCreateTokenRequestDto;
import tech.dut.fasto.web.rest.user.vnpay.dto.request.VNPayCreateUrlRequestDto;
import tech.dut.fasto.web.rest.user.vnpay.dto.response.UserCardResponseDto;
import tech.dut.fasto.web.rest.user.vnpay.dto.response.VNPayCreateUrlResponseDto;
import tech.dut.fasto.web.rest.user.vnpay.dto.response.VNPayReturnUrlResponseDto;
import tech.dut.fasto.web.rest.user.vnpay.dto.response.VNPayTokenResponseDto;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

public interface VnPayUserService {
    VNPayCreateUrlResponseDto createPaymentUrl(VNPayCreateUrlRequestDto requestDTO, HttpServletRequest request);

    VNPayReturnUrlResponseDto getReturnPaymentUrl(HttpServletRequest request) throws UnsupportedEncodingException;

    VNPayReturnUrlResponseDto getIPNUrl(HttpServletRequest request) throws UnsupportedEncodingException;

    void handleSuccessPayment(Long billId,Long shopId, HttpServletRequest request);

    void handleFailPayment(Long billId,Long shopId, HttpServletRequest request);

    VNPayTokenResponseDto createToken(HttpServletRequest request, VNPayCreateTokenRequestDto vnPayCreateTokenRequestDto);

    VNPayCreateUrlResponseDto createTokenPay(VNPayCreateUrlRequestDto requestDTO, HttpServletRequest request);

    VNPayReturnUrlResponseDto getTokenUrl(HttpServletRequest request, Map fields);

    VNPayCreateUrlResponseDto createTokenPayment(HttpServletRequest request, VNPayCreateTokenPaymentUrlRequestDto requestDTO);

    VNPayTokenResponseDto deleteToken(HttpServletRequest request, Long id);

    void handleGetTokenSuccess(Long userId, Map fields);

    VNPayReturnUrlResponseDto handleValidatePayment(Map fields, HttpServletRequest request);

    List<UserCardResponseDto> getAllListCards();
}
