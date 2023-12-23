package tech.dut.fasto.web.rest.shop.image.service;

import tech.dut.fasto.web.rest.shop.image.dto.request.ShopImageRequestDto;

import java.util.List;

public interface ShopImageService {

    void createImageForVoucher(ShopImageRequestDto shopImageRequestDto);

    void createImageForShop(ShopImageRequestDto shopImageRequestDto);

    void createImageForProduct(ShopImageRequestDto shopImageRequestDto);

    List<String> getImageForVoucher(Long id);

    List<String> getImageForShop(Long id);

    List<String> getImageForProduct(Long id);
}
