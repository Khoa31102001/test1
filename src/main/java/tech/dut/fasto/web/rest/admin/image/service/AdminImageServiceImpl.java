package tech.dut.fasto.web.rest.admin.image.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.dut.fasto.common.domain.Voucher;
import tech.dut.fasto.common.domain.VoucherImage;
import tech.dut.fasto.common.domain.enumeration.VoucherProvider;
import tech.dut.fasto.common.repository.VoucherImageRepository;
import tech.dut.fasto.common.repository.VoucherRepository;

import tech.dut.fasto.common.service.impl.MessageService;
import tech.dut.fasto.errors.FastoAlertException;
import tech.dut.fasto.web.rest.admin.image.dto.request.AdminImageVoucherRequestDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminImageServiceImpl implements AdminImageService {
    private final VoucherRepository voucherRepository;

    private final VoucherImageRepository voucherImageRepository;

    private final MessageService messageService;

    @Override
    @Transactional(rollbackFor = FastoAlertException.class)
    public void createImageForVoucher(AdminImageVoucherRequestDto adminImageVoucherRequestDto) {
        Optional<Voucher> voucherOptional = voucherRepository.findByIdAndUserType(adminImageVoucherRequestDto.getId(), VoucherProvider.ADMIN);

        if (voucherOptional.isPresent() && !adminImageVoucherRequestDto.getImages().isEmpty()) {
            Voucher voucher = voucherOptional.get();
            List<VoucherImage> voucherImageList = new ArrayList<>();
            for(String item : adminImageVoucherRequestDto.getImages()){
                VoucherImage voucherImage = new VoucherImage();
                voucherImage.setImage(item);
                voucherImage.setVoucher(voucher);
                voucherImageList.add(voucherImage);
            }
            voucherImageRepository.saveAll(voucherImageList);
        }
        else{
            throw new FastoAlertException(this.messageService.getMessage("error.code.voucher.create.image.failed"), this.messageService.getMessage("error.voucher.not.found"));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getImageForVoucher(Long id) {
        return voucherImageRepository.findAllByVoucherId(id).stream().map(VoucherImage::getImage).toList();
    }
}
