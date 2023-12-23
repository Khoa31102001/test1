package tech.dut.fasto.web.rest.user.product.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tech.dut.fasto.web.rest.shop.product.dto.response.ShopProductResponseDto;
import tech.dut.fasto.web.rest.user.product.dto.response.UserProductResponseDto;

public interface UserProductService {

    Page<UserProductResponseDto> getTopProductShop(Pageable pageable, Long shopId, String query);

    Page<ShopProductResponseDto> getProductShop(Pageable pageable, Long shopId, String query);
    UserProductResponseDto getDetail(Long id);

    Page<UserProductResponseDto> getAllProduct(Pageable pageable, String query, Double x, Double y);

    Page<UserProductResponseDto> getTopProduct(Pageable pageable);
}
