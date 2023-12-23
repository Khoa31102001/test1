package tech.dut.fasto.web.rest.admin.shop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.dut.fasto.common.domain.Shop;
import tech.dut.fasto.common.domain.enumeration.ShopSchedule;
import tech.dut.fasto.common.domain.enumeration.UserStatus;
import tech.dut.fasto.common.repository.ShopRepository;
import tech.dut.fasto.common.repository.UserRepository;
import tech.dut.fasto.common.service.EmailService;

import tech.dut.fasto.common.service.impl.MessageService;
import tech.dut.fasto.errors.FastoAlertException;
import tech.dut.fasto.web.rest.admin.shop.Mapper.AdminShopDtoMapper;
import tech.dut.fasto.web.rest.admin.shop.dto.AdminShopResponseDto;

@Service
@RequiredArgsConstructor
public class AdminShopServiceImpl implements AdminShopService {
    private final UserRepository userRepository;

    private final ShopRepository shopRepository;

    private final EmailService emailService;

    private final AdminShopDtoMapper adminShopDtoMapper;

    private final MessageService messageService;
    
    @Override
    @Transactional(rollbackFor = FastoAlertException.class)
    public void blockShop(Long shopId) {
        Shop shop = shopRepository.findById(shopId).orElseThrow(() -> new FastoAlertException(this.messageService.getMessage("error.code.shop.block.failed"), this.messageService.getMessage("error.shop.not.found")));
        if (!shop.getUser().getStatus().equals(UserStatus.ACTIVATED)) {
            throw new FastoAlertException(this.messageService.getMessage("error.code.shop.block.failed"), this.messageService.getMessage("error.shop.is.inactive"));
        }
        shop.getUser().setStatus(UserStatus.BLOCKED);
    }

    @Override
    @Transactional(rollbackFor = FastoAlertException.class)
    public void activeShop(Long shopId) {
        Shop shop = shopRepository.findById(shopId).orElseThrow(() -> new FastoAlertException(this.messageService.getMessage("error.code.shop.active.failed"), this.messageService.getMessage("error.shop.not.found")));
        this.emailService.sendEmailForActiveShop(shop.getUser().getEmail(), shop.getName());
        shop.getUser().setStatus(UserStatus.ACTIVATED);
        shop.setScheduleActive(ShopSchedule.CLOSE);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AdminShopResponseDto> getAllShops(Pageable pageable, UserStatus status, String query) {
        if (status != null) {
            return shopRepository.getAllShop(pageable, status, query);
        }
        return shopRepository.getAllShop(pageable, query);
    }

    @Override
    @Transactional(readOnly = true)
    public AdminShopResponseDto getDetailShop(Long id) {
        return adminShopDtoMapper.toDto(shopRepository.findById(id).orElseThrow(() -> new FastoAlertException(this.messageService.getMessage("error.code.shop.get.detail.failed"), this.messageService.getMessage("error.shop.not.found"))));
    }
}
