package tech.dut.fasto.web.rest.admin.product;

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
import tech.dut.fasto.common.domain.enumeration.ProductStatus;
import tech.dut.fasto.config.security.AuthoritiesConstants;
import tech.dut.fasto.web.rest.admin.product.dto.response.AdminProductResponseDto;
import tech.dut.fasto.web.rest.admin.product.service.AdminProductService;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/admin/management")
@RequiredArgsConstructor
@Validated
@PreAuthorize("hasAuthority('" + AuthoritiesConstants.ADMIN + "')")
public class AdminManageProductResource {

    private final AdminProductService adminProductService;

    @ApiOperation("API for get all products")
    @GetMapping(path = "/products")
    @SwaggerPageable
    public ResponseEntity<Page<AdminProductResponseDto>> getAllProducts(@PageableDefault(sort = "modifiedAt", direction = Sort.Direction.DESC)
                                                           Pageable pageable,
                                                                        @ApiParam(value = "Status")
                                                           @RequestParam(required = false) ProductStatus status,
                                                                        @ApiParam(value = "Search by name")
                                                           @RequestParam(required = false) String query) {
        return ResponseEntity.ok().body(this.adminProductService.getAllProducts(pageable, status, query));
    }

    @ApiOperation("API for get all products of shop")
    @GetMapping(path = "/products/shop/{id}")
    @SwaggerPageable
    public ResponseEntity<Page<AdminProductResponseDto>> getAllProductsShop(@PageableDefault(sort = "modifiedAt", direction = Sort.Direction.DESC)
                                                                        Pageable pageable,
                                                                        @ApiParam(value = "Status")
                                                                        @RequestParam(required = false) ProductStatus status,
                                                                        @ApiParam(value = "Search by name")
                                                                        @RequestParam(required = false) String query,@NotNull @PathVariable("id") Long id) {
        return ResponseEntity.ok().body(this.adminProductService.getAllProductShop(pageable, status, query, id));
    }


    @ApiOperation("API for Shop get detail product")
    @GetMapping("/product/detail/{id}")
    public ResponseEntity<AdminProductResponseDto> getDetailProduct(@NotNull @PathVariable("id") Long id) {
        return ResponseEntity.ok().body(this.adminProductService.getDetail(id));
    }
}
