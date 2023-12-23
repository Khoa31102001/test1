package tech.dut.fasto.web.rest.user.favorite.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tech.dut.fasto.web.rest.user.favorite.dto.response.ShopFavoriteResponseDto;

public interface UserFavoriteService {
    void createFavouriteShop(Long shopId);
    void deleteFavouriteShop(Long id);
    Page<ShopFavoriteResponseDto> getAllFavouriteUsers(Pageable pageable);
}
