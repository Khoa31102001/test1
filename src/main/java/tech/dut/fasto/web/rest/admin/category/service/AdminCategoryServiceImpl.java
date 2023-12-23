package tech.dut.fasto.web.rest.admin.category.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.dut.fasto.common.domain.Category;
import tech.dut.fasto.common.dto.CategoryDto;
import tech.dut.fasto.common.repository.CategoryRepository;

import tech.dut.fasto.common.service.impl.MessageService;
import tech.dut.fasto.errors.FastoAlertException;
import tech.dut.fasto.errors.FastoAlertException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AdminCategoryServiceImpl implements AdminCategoryService {

    CategoryRepository categoryRepository;
    MessageService messageService;
    
    @Override
    @Transactional(rollbackFor = FastoAlertException.class)
    public void createCategory(CategoryDto categoryDto) {
        if (Boolean.TRUE.equals(categoryRepository.existsByName(categoryDto.getName()))) {
            throw new FastoAlertException(this.messageService.getMessage("error.code.categories.create.failed"), this.messageService.getMessage("error.categories.existed"));
        }
        Category category = new Category();
        category.setName(categoryDto.getName());
        category.setCategoryImage(categoryDto.getCategoryImage());
        categoryRepository.save(category);
    }

    @Override
    @Transactional(rollbackFor = FastoAlertException.class)
    public void deleteCategory(Long categoryId) {

        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
        if (categoryOptional.isPresent()) {
            categoryOptional.get().setDeleteFlag(true);
            categoryOptional.get().getProducts().forEach(p -> p.setDeleteFlag(true));
        } else {
            throw new FastoAlertException(this.messageService.getMessage("error.code.categories.delete.failed"), this.messageService.getMessage("error.categories.not.existed"));
        }
    }

    @Override
    @Transactional(rollbackFor = FastoAlertException.class)
    public void updateCategory(CategoryDto categoryDto, Long id) {
        Optional<Category> categoryOptional = categoryRepository.findByIdAndDeleteFlagIsFalse(id);
        if (categoryOptional.isPresent()) {
            if(null != categoryDto.getCategoryImage()){
                categoryOptional.get().setCategoryImage(categoryDto.getCategoryImage());
            }
            if(null != categoryDto.getName()){
                categoryOptional.get().setName(categoryDto.getName());
            }
        } else {
            throw new FastoAlertException(this.messageService.getMessage("error.code.categories.update.failed"), this.messageService.getMessage("error.categories.not.existed"));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CategoryDto> getAllCategory(Pageable pageable, String query) {
        return categoryRepository.getAllCategoriesByAdmin(pageable,query);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDto getDetailCategory(Long id) {
        CategoryDto categoryDto = new CategoryDto();
       Optional<Category> categoryOptional = categoryRepository.findByIdAndDeleteFlagIsFalse(id);
       if(categoryOptional.isPresent()) {
           categoryDto.setId(id);
           categoryDto.setName(categoryOptional.get().getName());
           categoryDto.setCategoryImage(categoryOptional.get().getCategoryImage());
           categoryDto.setDeleteFlag(categoryOptional.get().getDeleteFlag());
       }
       else{
           throw new FastoAlertException(this.messageService.getMessage("error.code.categories.get.detail.failed"), this.messageService.getMessage("error.categories.not.existed"));
       }
        return categoryDto;
    }
}
