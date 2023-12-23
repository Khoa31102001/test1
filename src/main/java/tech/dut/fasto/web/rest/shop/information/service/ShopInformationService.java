package tech.dut.fasto.web.rest.shop.information.service;

import tech.dut.fasto.web.rest.shop.information.dto.request.ShopInformationRequestDto;
import tech.dut.fasto.web.rest.shop.information.dto.response.ShopProfileResponseDto;

public interface ShopInformationService {
    ShopProfileResponseDto getShopInfo();

    void updateShopInfo(ShopInformationRequestDto shopInformationRequestDto);
}
