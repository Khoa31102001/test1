package tech.dut.fasto.web.rest.shop.voucher.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tech.dut.fasto.common.domain.enumeration.VoucherStatus;
import tech.dut.fasto.web.rest.shop.voucher.dto.request.ShopVoucherRequestDto;
import tech.dut.fasto.web.rest.shop.voucher.dto.response.ShopVoucherResponseDto;


public interface ShopVoucherService {

     Page<ShopVoucherResponseDto> getAllVoucher(Pageable pageable, String query, VoucherStatus status);

     void createVoucher(ShopVoucherRequestDto shopVoucherRequestDto);

     void deleteVoucher(Long voucherId) ;

     void updateVoucher(ShopVoucherRequestDto shopVoucherRequestDto, Long id);

     ShopVoucherResponseDto getDetail(Long id);
}
