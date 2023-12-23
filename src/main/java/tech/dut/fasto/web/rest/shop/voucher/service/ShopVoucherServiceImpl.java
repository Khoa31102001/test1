package tech.dut.fasto.web.rest.shop.voucher.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.dut.fasto.common.domain.Shop;
import tech.dut.fasto.common.domain.Voucher;
import tech.dut.fasto.common.domain.enumeration.UserStatus;
import tech.dut.fasto.common.domain.enumeration.VoucherProvider;
import tech.dut.fasto.common.domain.enumeration.VoucherStatus;
import tech.dut.fasto.common.domain.enumeration.VoucherType;
import tech.dut.fasto.common.repository.ShopRepository;
import tech.dut.fasto.common.repository.VoucherRepository;

import tech.dut.fasto.common.service.impl.MessageService;
import tech.dut.fasto.config.security.SecurityUtils;
import tech.dut.fasto.errors.FastoAlertException;
import tech.dut.fasto.web.rest.shop.voucher.dto.request.ShopVoucherRequestDto;
import tech.dut.fasto.web.rest.shop.voucher.dto.response.ShopVoucherResponseDto;
import tech.dut.fasto.web.rest.shop.voucher.mapper.ShopVoucherEntityMapper;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ShopVoucherServiceImpl implements ShopVoucherService {

    private final ShopRepository shopRepository;

    private final VoucherRepository voucherRepository;

    private final ShopVoucherEntityMapper shopVoucherEntityMapper;

    private final MessageService messageService;

    @Override
    @Transactional(readOnly = true)
    public Page<ShopVoucherResponseDto> getAllVoucher(Pageable pageable, String query, VoucherStatus status) {

        String email = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.voucher.get.failed"), messageService.getMessage("error.authenticate.unauthorized.user")));
        Shop shop = shopRepository.findByUserEmailAndUserStatus(email, UserStatus.ACTIVATED).orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.voucher.get.failed"), messageService.getMessage("error.shop.is.inactive")));

        return voucherRepository.getAllVouchers(pageable, shop.getId(), query, status);
    }

    @Override
    @Transactional(rollbackFor = FastoAlertException.class)
    public void createVoucher(ShopVoucherRequestDto shopVoucherRequestDto) {

        if (shopVoucherRequestDto.getStartedAt() > shopVoucherRequestDto.getEndedAt()) {
            throw new FastoAlertException(messageService.getMessage("error.code.voucher.create.failed"), messageService.getMessage("error.voucher.start.date.than.end.date"));
        }

        if((shopVoucherRequestDto.getValueNeed().compareTo(shopVoucherRequestDto.getValueDiscount())<0 && shopVoucherRequestDto.getVoucherType().equals(VoucherType.PRICE) ) || shopVoucherRequestDto.getValueNeed().compareTo(shopVoucherRequestDto.getMaxDiscount())<0 && shopVoucherRequestDto.getVoucherType().equals(VoucherType.PRICE)){
            throw new FastoAlertException(messageService.getMessage("error.code.voucher.create.failed"),messageService.getMessage("error.voucher.value.need.greater.than.value.discount.or.max.discount"));
        }

        if(shopVoucherRequestDto.getVoucherType().equals(VoucherType.PERCENT) && shopVoucherRequestDto.getValueDiscount().compareTo(BigDecimal.valueOf(100L))>0){
            throw new FastoAlertException(messageService.getMessage("error.code.voucher.create.failed"),messageService.getMessage("error.voucher.value.discount.not.than.one.hundred"));
        }

//        if(Instant.now().truncatedTo(ChronoUnit.DAYS).isAfter(Instant.ofEpochSecond(shopVoucherRequestDto.getEndedAt()).truncatedTo(ChronoUnit.DAYS))){
//            throw new FastoAlertException(messageService.getMessage("error.code.voucher.create.failed"), messageService.getMessage("error.voucher.end.date.not.before.now"));
//        }
        if (Instant.now().truncatedTo(ChronoUnit.DAYS).toEpochMilli()>Instant.ofEpochSecond(shopVoucherRequestDto.getEndedAt()).truncatedTo(ChronoUnit.DAYS).toEpochMilli()) {
            throw new FastoAlertException(messageService.getMessage("error.code.voucher.create.failed"), messageService.getMessage("error.voucher.end.date.not.before.now"));
        }

        String email = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.voucher.create.failed"), messageService.getMessage("error.authenticate.unauthorized.user")));
        Shop shop = shopRepository.findByUserEmailAndUserStatus(email, UserStatus.ACTIVATED).orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.voucher.create.failed"), messageService.getMessage("error.shop.is.inactive")));
        Voucher voucher = shopVoucherEntityMapper.toEntity(shopVoucherRequestDto);
        if(Instant.now().truncatedTo(ChronoUnit.DAYS).isAfter(voucher.getStartedAt().truncatedTo(ChronoUnit.DAYS))){
            voucher.setStatus(VoucherStatus.ACTIVATED);
        }else{
            voucher.setStatus(VoucherStatus.INACTIVE);
        }


        voucher.setShop(shop);
        voucher.setUserType(VoucherProvider.SHOP);
        voucherRepository.save(voucher);
    }

    @Override
    @Transactional(rollbackFor = FastoAlertException.class)
    public void deleteVoucher(Long voucherId) {
        String email = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.voucher.delete.failed"), messageService.getMessage("error.authenticate.unauthorized.user")));
        Shop shop = shopRepository.findByUserEmailAndUserStatus(email, UserStatus.ACTIVATED).orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.voucher.delete.failed"), messageService.getMessage("error.shop.is.inactive")));
        Optional<Voucher> voucherOptional = voucherRepository.findByIdAndShopId(voucherId, shop.getId());
        if (voucherOptional.isPresent()) {
            Voucher voucher = voucherOptional.get();
            voucher.setStatus(VoucherStatus.DELETED);
        } else {
            throw new FastoAlertException(messageService.getMessage("error.code.voucher.delete.failed"), messageService.getMessage("error.voucher.not.existed"));
        }
    }

    @Override
    @Transactional(rollbackFor = FastoAlertException.class)
    public void updateVoucher(ShopVoucherRequestDto shopVoucherRequestDto, Long id) {
        if (shopVoucherRequestDto.getStartedAt().equals(shopVoucherRequestDto.getEndedAt())) {
            throw new FastoAlertException(messageService.getMessage("error.code.voucher.update.failed"), messageService.getMessage("error.voucher.start.date.than.end.date"));
        }

        String email = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.voucher.update.failed"), messageService.getMessage("error.authenticate.unauthorized.user")));
        Shop shop = shopRepository.findByUserEmailAndUserStatus(email, UserStatus.ACTIVATED).orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.voucher.update.failed"), messageService.getMessage("error.shop.is.inactive")));

        Optional<Voucher> voucherOptional = voucherRepository.findByIdAndShopId(id, shop.getId());
        if(voucherOptional.isPresent()) {
            shopVoucherEntityMapper.partialUpdate(voucherOptional.get(), shopVoucherRequestDto);
        }
        else {
            throw new FastoAlertException(messageService.getMessage("error.code.voucher.update.failed"),messageService.getMessage("error.voucher.not.existed"));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ShopVoucherResponseDto getDetail(Long id) {
        String email = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.voucher.get.detail.failed"), messageService.getMessage("error.authenticate.unauthorized.user")));
        Shop shop = shopRepository.findByUserEmailAndUserStatus(email, UserStatus.ACTIVATED).orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.voucher.get.detail.failed"), messageService.getMessage("error.shop.is.inactive")));
        ShopVoucherResponseDto shopVoucherResponseDto = new ShopVoucherResponseDto();

        Optional<Voucher> voucherOptional = voucherRepository.findByIdAndShopId(id, shop.getId());
        if(voucherOptional.isPresent()){
            BeanUtils.copyProperties(voucherOptional.get(), shopVoucherResponseDto);
        }
        else{
            throw new FastoAlertException(messageService.getMessage("error.code.voucher.get.detail.failed"), messageService.getMessage("error.voucher.not.existed"));
        }
        return shopVoucherResponseDto;
    }

}
