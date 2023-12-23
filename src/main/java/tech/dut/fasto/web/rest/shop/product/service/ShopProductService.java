package tech.dut.fasto.web.rest.shop.product.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tech.dut.fasto.common.domain.enumeration.ProductStatus;
import tech.dut.fasto.web.rest.shop.product.dto.request.ShopProductRequestDto;
import tech.dut.fasto.web.rest.shop.product.dto.response.ShopProductResponseDto;

public interface ShopProductService {
     void createProduct(ShopProductRequestDto shopProductRequestDto);

     Page<ShopProductResponseDto> getAllProducts(Pageable pageable, ProductStatus status, String query);

     void deleteProduct(Long productId);

     void updateProduct(ShopProductRequestDto shopProductRequestDto);

    ShopProductResponseDto getDetail(Long id);
}
