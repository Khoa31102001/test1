package tech.dut.fasto.web.rest.shop.rating.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tech.dut.fasto.web.rest.shop.rating.dto.response.ShopRatingResponseDto;

public interface ShopRatingService {

    Page<ShopRatingResponseDto> getAllRatings(Pageable pageable);
}
