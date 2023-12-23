package tech.dut.fasto.web.rest.shop.category.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tech.dut.fasto.common.dto.CategoryDto;

public interface ShopCategoryService {
    Page<CategoryDto> getAllCategories(Pageable pageable, Boolean deleteFlag);
}
