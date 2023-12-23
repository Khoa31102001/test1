package tech.dut.fasto.web.rest.user.ratings.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.dut.fasto.common.domain.Bill;
import tech.dut.fasto.common.domain.Rating;
import tech.dut.fasto.common.domain.Shop;
import tech.dut.fasto.common.domain.User;
import tech.dut.fasto.common.domain.enumeration.UserStatus;
import tech.dut.fasto.common.repository.BillRepository;
import tech.dut.fasto.common.repository.RatingRepository;
import tech.dut.fasto.common.repository.ShopRepository;
import tech.dut.fasto.common.repository.UserRepository;

import tech.dut.fasto.common.service.impl.MessageService;
import tech.dut.fasto.config.security.SecurityUtils;
import tech.dut.fasto.errors.FastoAlertException;
import tech.dut.fasto.web.rest.shop.rating.dto.response.ShopRatingResponseDto;
import tech.dut.fasto.web.rest.user.ratings.dto.request.UserFeedBackShopRequestDto;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserRatingServiceImpl implements UserRatingService {

    private final ShopRepository shopRepository;

    private final RatingRepository ratingsRepository;

    private final UserRepository userRepository;

    private final BillRepository billRepository;

    private final MessageService messageService;

    @Override
    @Transactional(rollbackFor =FastoAlertException.class )
    public void feedBackStart(UserFeedBackShopRequestDto userFeedBackShopRequestDto) {

        String email = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.feedback.create.failed"), messageService.getMessage("error.authenticate.unauthorized.user")));
        User useTmp = userRepository.findByEmailAndStatus(email, UserStatus.ACTIVATED).orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.feedback.create.failed"), messageService.getMessage("error.authenticate.user.already.activated")));

        Shop shop = shopRepository.findById(userFeedBackShopRequestDto.getShopId()).orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.feedback.create.failed"),messageService.getMessage("error.shop.not.found")));

        if (shop.getStartRatings() == null) {
            shop.setStartRatings(userFeedBackShopRequestDto.getStart());
            shop.setRatings(1D);
        }else{
            Double start = shop.getStartRatings() * shop.getRatings();
            start = (start + userFeedBackShopRequestDto.getStart()) / (shop.getRatings() + 1D);
            shop.setRatings(shop.getRatings() + 1D);
            shop.setStartRatings(start);
        }


        Bill bill = billRepository.findById(userFeedBackShopRequestDto.getBillId()).orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.feedback.create.failed"),messageService.getMessage("error.bill.not.found")));
        bill.setIsRating(true);
        bill.setRatings(userFeedBackShopRequestDto.getStart());

        Rating ratings = new Rating();
        Long userId = useTmp.getId();
        Optional<User> optionalUser = userRepository.findById(userId);
        User user = optionalUser.orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.feedback.create.failed"),messageService.getMessage("error.user.not.found")));
        ratings.setRatings(userFeedBackShopRequestDto.getStart());
        ratings.setShop(shop);
        ratings.setUser(user);
        ratings.setContent(userFeedBackShopRequestDto.getContent());
        ratingsRepository.save(ratings);
    }

    @Override
    public Page<ShopRatingResponseDto> getAllRatingUser(Long shopId, Pageable pageable) {
        return ratingsRepository.getAllRatingShop(pageable,shopId);
    }
}
