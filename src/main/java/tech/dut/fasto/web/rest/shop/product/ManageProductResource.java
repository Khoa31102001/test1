package tech.dut.fasto.web.rest.shop.product;

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
import tech.dut.fasto.web.rest.shop.product.dto.request.ShopProductRequestDto;
import tech.dut.fasto.web.rest.shop.product.dto.response.ShopProductResponseDto;
import tech.dut.fasto.web.rest.shop.product.service.ShopProductService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/shop/management")
@RequiredArgsConstructor
@Validated
@PreAuthorize("hasAuthority('" + AuthoritiesConstants.SHOP + "')")
public class ManageProductResource {

    private final ShopProductService shopProductService;

    @ApiOperation("API for Shop add product")
    @PostMapping("/product")
    public ResponseEntity<String> createProduct(@Valid @RequestBody ShopProductRequestDto shopProductRequestDto) {
        shopProductService.createProduct(shopProductRequestDto);
        return ResponseEntity.ok().build();
    }

    @ApiOperation("API for get all products")
    @GetMapping(path = "/products")
    @SwaggerPageable
    public ResponseEntity<Page<ShopProductResponseDto>> getAllProducts(@PageableDefault(sort = "modifiedAt", direction = Sort.Direction.DESC)
                                                           Pageable pageable,
                                                                       @ApiParam(value = "Status")
                                                           @RequestParam(required = false) ProductStatus status,
                                                                       @ApiParam(value = "Search by name")
                                                           @RequestParam(required = false) String query) {
        return ResponseEntity.ok().body(this.shopProductService.getAllProducts(pageable, status, query));
    }

    @ApiOperation("API for Shop delete product")
    @DeleteMapping("/product/delete/{id}")
    public ResponseEntity<String> deleteProduct(@NotNull @PathVariable("id") Long id) {
        this.shopProductService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }

    @ApiOperation("API for Shop update product")
    @PutMapping("/product/update")
    public ResponseEntity<String> updateProduct(@Valid @RequestBody ShopProductRequestDto shopProductRequestDto) {
        this.shopProductService.updateProduct(shopProductRequestDto);
        return ResponseEntity.ok().build();
    }

    @ApiOperation("API for Shop get detail product")
    @GetMapping("/product/detail/{id}")
    public ResponseEntity<ShopProductResponseDto> getDetailProduct(@NotNull @PathVariable("id") Long id) {
        return ResponseEntity.ok().body(this.shopProductService.getDetail(id));
    }
}
