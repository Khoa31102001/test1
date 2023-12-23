package tech.dut.fasto.web.rest.admin.voucher.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tech.dut.fasto.common.domain.enumeration.VoucherStatus;
import tech.dut.fasto.web.rest.admin.voucher.dto.request.AdminVoucherRequestDto;
import tech.dut.fasto.web.rest.admin.voucher.dto.response.AdminVoucherResponseDto;
import tech.dut.fasto.web.rest.shop.voucher.dto.response.ShopVoucherResponseDto;

public interface AdminVoucherService {
    void createVoucher(AdminVoucherRequestDto adminVoucherRequestDto);

    void deleteVoucher(Long voucherId);

    void updateVoucher(AdminVoucherRequestDto adminVoucherRequestDto, Long id);

    Page<AdminVoucherResponseDto> getAllVoucher(Pageable pageable, String query, VoucherStatus status);

    AdminVoucherResponseDto getDetailVoucher(Long id);

    ShopVoucherResponseDto getDetailVoucherShop(Long id);

    Page<ShopVoucherResponseDto> getAllVoucherShop(Pageable pageable, Long id, String query, VoucherStatus status);
}
