package tech.dut.fasto.web.rest.admin.product.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tech.dut.fasto.common.domain.enumeration.ProductStatus;
import tech.dut.fasto.web.rest.admin.product.dto.response.AdminProductResponseDto;

public interface AdminProductService {

     Page<AdminProductResponseDto> getAllProducts(Pageable pageable, ProductStatus status, String query);

    AdminProductResponseDto getDetail(Long id);

    Page<AdminProductResponseDto> getAllProductShop(Pageable pageable, ProductStatus status, String query, Long shopId);

}
