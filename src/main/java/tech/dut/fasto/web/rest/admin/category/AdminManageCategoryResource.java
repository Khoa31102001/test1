package tech.dut.fasto.web.rest.admin.category;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tech.dut.fasto.common.anotation.SwaggerPageable;
import tech.dut.fasto.common.dto.CategoryDto;
import tech.dut.fasto.config.security.AuthoritiesConstants;
import tech.dut.fasto.web.rest.admin.category.service.AdminCategoryService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/admin/management")
@RequiredArgsConstructor
@Validated
@PreAuthorize("hasAuthority('" + AuthoritiesConstants.ADMIN + "')")
public class AdminManageCategoryResource {

    private final AdminCategoryService adminCategoryService;

    @ApiOperation("API for get all products")
    @GetMapping(path = "/categories")
    @SwaggerPageable
    public ResponseEntity<Page<CategoryDto>> getAllProducts(@PageableDefault(sort = "modifiedAt", direction = Sort.Direction.DESC)
                                                            Pageable pageable,
                                                            @ApiParam(value = "Query")
                                                            @RequestParam(required = false) String query) {
        return ResponseEntity.ok().body(this.adminCategoryService.getAllCategory(pageable, query));
    }


    @ApiOperation("API for get detail")
    @GetMapping(path = "/categories/{id}")
    public ResponseEntity<CategoryDto> getDetailCategory(@NotNull @PathVariable("id") Long id){
        return ResponseEntity.ok().body(this.adminCategoryService.getDetailCategory(id));
    }


    @ApiOperation("API for Admin delete category")
    @DeleteMapping("/category/{id}")
    public ResponseEntity<?> deleteCategory(@NotNull @PathVariable("id") Long id) {
        adminCategoryService.deleteCategory(id);
        return ResponseEntity.ok().build();
    }

    @ApiOperation("API for Admin update category")
    @PutMapping("/category/{id}")
    public ResponseEntity<?> updateCategory( @PathVariable("id") Long id ,@Valid @RequestBody CategoryDto categoryDto) {
        adminCategoryService.updateCategory(categoryDto, id);
        return ResponseEntity.ok().build();
    }

    @ApiOperation("API for Admin add category")
    @PostMapping("/category")
    public ResponseEntity<?> createCategory(@Valid @RequestBody CategoryDto categoryDto) {
        adminCategoryService.createCategory(categoryDto);
        return ResponseEntity.ok().build();
    }
}
