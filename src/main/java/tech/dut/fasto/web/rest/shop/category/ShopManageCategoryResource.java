package tech.dut.fasto.web.rest.shop.category;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.dut.fasto.common.anotation.SwaggerPageable;
import tech.dut.fasto.common.dto.CategoryDto;
import tech.dut.fasto.config.security.AuthoritiesConstants;
import tech.dut.fasto.web.rest.shop.category.service.ShopCategoryService;

@RestController
@RequestMapping("/shop/management")
@RequiredArgsConstructor
@Validated
@PreAuthorize("hasAuthority('" + AuthoritiesConstants.SHOP + "')")
public class ShopManageCategoryResource {

    private final ShopCategoryService shopCategoryService;

    @ApiOperation("API for get all products")
    @GetMapping(path = "/categories")
    @SwaggerPageable
    public ResponseEntity<Page<CategoryDto>> getAllProducts(@PageableDefault(sort = "modifiedAt", direction = Sort.Direction.DESC)
                                                            Pageable pageable,
                                                            @ApiParam(value = "Delete Flag")
                                                            @RequestParam(required = false) Boolean deleteFlag) {
        return ResponseEntity.ok().body(this.shopCategoryService.getAllCategories(pageable, deleteFlag));
    }
}
