package tech.dut.fasto.web.rest.admin.voucher.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.dut.fasto.common.domain.Voucher;
import tech.dut.fasto.common.domain.enumeration.VoucherProvider;
import tech.dut.fasto.common.domain.enumeration.VoucherStatus;
import tech.dut.fasto.common.domain.enumeration.VoucherType;
import tech.dut.fasto.common.repository.VoucherRepository;
import tech.dut.fasto.common.service.impl.MessageService;
import tech.dut.fasto.errors.FastoAlertException;
import tech.dut.fasto.web.rest.admin.voucher.dto.request.AdminVoucherRequestDto;
import tech.dut.fasto.web.rest.admin.voucher.dto.response.AdminVoucherResponseDto;
import tech.dut.fasto.web.rest.admin.voucher.mapper.AdminVoucherEntityMapper;
import tech.dut.fasto.web.rest.shop.voucher.dto.response.ShopVoucherResponseDto;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AdminVoucherServiceImpl implements AdminVoucherService {

    private final VoucherRepository voucherRepository;

    private final AdminVoucherEntityMapper adminVoucherEntityMapper;

    private final MessageService messageService;

    @Override
    @Transactional(rollbackFor = FastoAlertException.class)
    public void createVoucher(AdminVoucherRequestDto adminVoucherRequestDto) {
//        if (adminVoucherRequestDto.getStartedAt().compareTo(adminVoucherRequestDto.getEndedAt())>0) {
//            throw new FastoAlertException(this.messageService.getMessage("error.code.voucher.create.failed"), this.messageService.getMessage("error.voucher.start.date.than.end.date"));
//        }
        if (adminVoucherRequestDto.getStartedAt() > adminVoucherRequestDto.getEndedAt()) {
            throw new FastoAlertException(messageService.getMessage("error.code.voucher.create.failed"), messageService.getMessage("error.voucher.start.date.than.end.date"));
        }

        if ((adminVoucherRequestDto.getValueNeed().compareTo(adminVoucherRequestDto.getValueDiscount()) < 0 && adminVoucherRequestDto.getVoucherType().equals(VoucherType.PRICE)) || adminVoucherRequestDto.getValueNeed().compareTo(adminVoucherRequestDto.getMaxDiscount()) < 0 && adminVoucherRequestDto.getVoucherType().equals(VoucherType.PRICE)) {
            throw new FastoAlertException(messageService.getMessage("error.code.voucher.create.failed"), messageService.getMessage("error.voucher.value.need.greater.than.value.discount.or.max.discount"));
        }

        if (adminVoucherRequestDto.getVoucherType().equals(VoucherType.PERCENT) && adminVoucherRequestDto.getValueDiscount().compareTo(BigDecimal.valueOf(100L)) > 0) {
            throw new FastoAlertException(messageService.getMessage("error.code.voucher.create.failed"), messageService.getMessage("error.voucher.value.discount.not.than.one.hundred"));
        }

//        if(Instant.now().truncatedTo(ChronoUnit.DAYS).isAfter(Instant.ofEpochSecond(adminVoucherRequestDto.getEndedAt()).truncatedTo(ChronoUnit.DAYS))){
//            throw new FastoAlertException(messageService.getMessage("error.code.voucher.create.failed"), messageService.getMessage("error.voucher.end.date.not.before.now"));
//        }

        if (Instant.now().truncatedTo(ChronoUnit.DAYS).toEpochMilli()>Instant.ofEpochSecond(adminVoucherRequestDto.getEndedAt()).truncatedTo(ChronoUnit.DAYS).toEpochMilli()) {
            throw new FastoAlertException(messageService.getMessage("error.code.voucher.create.failed"), messageService.getMessage("error.voucher.end.date.not.before.now"));
        }

        Voucher voucher = adminVoucherEntityMapper.toEntity(adminVoucherRequestDto);
        voucher.setUserType(VoucherProvider.ADMIN);
        voucherRepository.save(voucher);
    }

    @Override
    @Transactional(rollbackFor = FastoAlertException.class)
    public void deleteVoucher(Long voucherId) {
        Optional<Voucher> voucherOptional = voucherRepository.findByIdAndUserType(voucherId, VoucherProvider.ADMIN);
        if (voucherOptional.isPresent()) {
            voucherOptional.get().setStatus(VoucherStatus.DELETED);
        } else {
            throw new FastoAlertException(this.messageService.getMessage("error.code.voucher.delete.failed"), this.messageService.getMessage("error.voucher.not.existed"));
        }
    }

    @Override
    @Transactional(rollbackFor = FastoAlertException.class)
    public void updateVoucher(AdminVoucherRequestDto adminVoucherRequestDto, Long id) {
        if (adminVoucherRequestDto.getStartedAt().equals(adminVoucherRequestDto.getEndedAt())) {
            throw new FastoAlertException(this.messageService.getMessage("error.code.voucher.update.failed"), this.messageService.getMessage("error.voucher.start.date.than.end.date"));
        }


        Optional<Voucher> voucherOptional = voucherRepository.findByIdAndUserType(id, VoucherProvider.ADMIN);
        if (voucherOptional.isPresent()) {
            adminVoucherEntityMapper.partialUpdate(voucherOptional.get(), adminVoucherRequestDto);
        } else {
            throw new FastoAlertException(this.messageService.getMessage("error.code.voucher.update.failed"), this.messageService.getMessage("error.voucher.not.existed"));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AdminVoucherResponseDto> getAllVoucher(Pageable pageable, String query, VoucherStatus status) {
        return voucherRepository.getAllVoucherByAdmin(pageable, VoucherProvider.ADMIN, query, status);
    }

    @Override
    @Transactional(readOnly = true)
    public AdminVoucherResponseDto getDetailVoucher(Long id) {
        if (!voucherRepository.existsById(id)) {
            throw new FastoAlertException(this.messageService.getMessage("error.code.voucher.get.detail.failed"), this.messageService.getMessage("error.voucher.not.found"));
        }
        return voucherRepository.getDetailVoucherByAdmin(VoucherProvider.ADMIN, id);
    }

    @Override
    @Transactional(readOnly = true)
    public ShopVoucherResponseDto getDetailVoucherShop(Long id) {
        if (!voucherRepository.existsById(id)) {
            throw new FastoAlertException(this.messageService.getMessage("error.code.voucher.get.detail.failed"), this.messageService.getMessage("error.voucher.not.found"));
        }
        return voucherRepository.getDetailVoucherByAdmin(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ShopVoucherResponseDto> getAllVoucherShop(Pageable pageable, Long id, String query, VoucherStatus status) {
        return voucherRepository.getAllVouchers(pageable, id, query, status);
    }
}
