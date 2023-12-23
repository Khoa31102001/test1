package tech.dut.fasto.web.rest.user.voucher.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tech.dut.fasto.common.domain.enumeration.VoucherProvider;
import tech.dut.fasto.web.rest.user.voucher.dto.request.UserVoucherProductRequestDto;
import tech.dut.fasto.web.rest.user.voucher.dto.response.VoucherUserResponseDto;

import java.util.List;

public interface UserVoucherService {
    List<VoucherUserResponseDto> getAllVoucherForBill(UserVoucherProductRequestDto userVoucherProductRequestDto);

    VoucherUserResponseDto getDetailVoucher(Long voucherId);

    Page<VoucherUserResponseDto> getAllVoucher(VoucherProvider voucherProvider, Pageable pageable, String query);

}
