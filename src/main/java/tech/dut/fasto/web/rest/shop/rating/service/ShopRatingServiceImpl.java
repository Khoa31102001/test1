package tech.dut.fasto.web.rest.shop.rating.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.dut.fasto.common.domain.Shop;
import tech.dut.fasto.common.domain.enumeration.UserStatus;
import tech.dut.fasto.common.repository.RatingRepository;
import tech.dut.fasto.common.repository.ShopRepository;

import tech.dut.fasto.common.service.impl.MessageService;
import tech.dut.fasto.config.security.SecurityUtils;
import tech.dut.fasto.errors.FastoAlertException;
import tech.dut.fasto.web.rest.shop.rating.dto.response.ShopRatingResponseDto;

@Service
@RequiredArgsConstructor
public class ShopRatingServiceImpl implements ShopRatingService {

    private final ShopRepository shopRepository;

    private final RatingRepository ratingRepository;

    private final MessageService messageService;


    @Override
    @Transactional(readOnly = true)
    public Page<ShopRatingResponseDto> getAllRatings(Pageable pageable) {

        String email = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.rating.get.failed"), messageService.getMessage("error.authenticate.unauthorized.user")));
        Shop shop = shopRepository.findByUserEmailAndUserStatus(email, UserStatus.ACTIVATED).orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.rating.get.failed"), messageService.getMessage("error.shop.is.inactive")));

        return ratingRepository.getAllRatingShop(pageable, shop.getId());
    }
}
