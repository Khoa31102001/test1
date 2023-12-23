package tech.dut.fasto.web.rest.user.ratings.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tech.dut.fasto.web.rest.shop.rating.dto.response.ShopRatingResponseDto;
import tech.dut.fasto.web.rest.user.ratings.dto.request.UserFeedBackShopRequestDto;

public interface UserRatingService {
    void feedBackStart(UserFeedBackShopRequestDto userFeedBackShopRequestDto);

    Page<ShopRatingResponseDto> getAllRatingUser(Long shopId, Pageable pageable);
}
