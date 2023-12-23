package tech.dut.fasto.web.rest.admin.category.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tech.dut.fasto.common.dto.CategoryDto;

public interface AdminCategoryService {
    void createCategory(CategoryDto categoryDto);

    void deleteCategory(Long categoryId);

    void updateCategory(CategoryDto categoryDto, Long id);


   Page<CategoryDto> getAllCategory(Pageable pageable, String query);
   CategoryDto getDetailCategory(Long id);
}
