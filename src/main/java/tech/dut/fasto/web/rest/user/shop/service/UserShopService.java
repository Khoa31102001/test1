package tech.dut.fasto.web.rest.user.shop.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestParam;
import tech.dut.fasto.web.rest.user.shop.dto.response.UserShopLocationResponseDto;
import tech.dut.fasto.web.rest.user.shop.dto.response.UserShopProfileResponseDto;
import tech.dut.fasto.web.rest.user.shop.dto.response.UserShopResponseDto;

import java.util.List;

public interface UserShopService {
    UserShopResponseDto getDetailShop(Long shopId, Double x, Double y);

    Page<UserShopLocationResponseDto> getAllLocationByCurrentLocation(Double x, Double y, Double radius, Pageable pageable);

    Page<UserShopLocationResponseDto> getAllShopLocationByCurrentLocation(Long shopId, Double radius, Pageable pageable);

    Page<UserShopProfileResponseDto> getAllShops(String query, Pageable pageable);

    void createRecentShop(Long shopId);

    Page<UserShopProfileResponseDto> getRecentShop(Pageable pageable);

    Page<UserShopProfileResponseDto> getTopShops(Pageable pageable);
}
