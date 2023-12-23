package tech.dut.fasto.web.rest.shop.category.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.dut.fasto.common.dto.CategoryDto;
import tech.dut.fasto.common.repository.CategoryRepository;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ShopCategoryServiceImpl implements ShopCategoryService {

    CategoryRepository categoryRepository;


    @Override
    @Transactional(readOnly = true)
    public Page<CategoryDto> getAllCategories(Pageable pageable, Boolean deleteFlag) {
        return categoryRepository.getAllCategories(pageable, deleteFlag);
    }
}
