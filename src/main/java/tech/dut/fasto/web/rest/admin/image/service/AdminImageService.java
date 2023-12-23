package tech.dut.fasto.web.rest.admin.image.service;

import tech.dut.fasto.web.rest.admin.image.dto.request.AdminImageVoucherRequestDto;

import java.util.List;

public interface AdminImageService {

    void createImageForVoucher(AdminImageVoucherRequestDto adminImageVoucherRequestDto);

    List<String> getImageForVoucher(Long id);
}
