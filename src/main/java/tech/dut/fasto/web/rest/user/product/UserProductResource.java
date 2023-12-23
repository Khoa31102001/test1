package tech.dut.fasto.web.rest.user.product;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tech.dut.fasto.web.rest.shop.product.dto.response.ShopProductResponseDto;
import tech.dut.fasto.web.rest.user.product.dto.response.UserProductResponseDto;
import tech.dut.fasto.web.rest.user.product.service.UserProductService;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/user/management/product")
@RequiredArgsConstructor
@Validated
public class UserProductResource {
    private final UserProductService userProductService;

    @ApiOperation("API for user get Top Product")
    @GetMapping("/shop/{shopId}")
    public ResponseEntity<Page<UserProductResponseDto>> getTopProductShop(@PageableDefault(sort = "modifiedAt", direction = Sort.Direction.DESC)
                                                                          Pageable pageable,
                                                                          @PathVariable("shopId") Long shopId,
                                                                          @ApiParam(value = "Search by name")
                                                                          @RequestParam(required = false) String query) {
        return ResponseEntity.ok(userProductService.getTopProductShop(pageable, shopId, query));
    }

    @ApiOperation("API for user get detail product")
    @GetMapping("/shop/detail/{id}")
    public ResponseEntity<UserProductResponseDto> getDetailProduct(@NotNull @PathVariable("id") Long id) {
        return ResponseEntity.ok(userProductService.getDetail(id));
    }

    @ApiOperation("API for user get all products shop")
    @GetMapping(path = "/{shopId}")
    public ResponseEntity<Page<ShopProductResponseDto>> getAllProducts(@PageableDefault(sort = "modifiedAt", direction = Sort.Direction.DESC)
                                                                       Pageable pageable,

                                                                       @PathVariable("shopId") Long shopId,
                                                                       @ApiParam(value = "Search by name")
                                                                       @RequestParam(required = false) String query) {
        return ResponseEntity.ok(this.userProductService.getProductShop(pageable, shopId, query));
    }

    @ApiOperation("API for user get all products")
    @GetMapping(path = "")
    public ResponseEntity<Page<UserProductResponseDto>> getAllProductsUser(@PageableDefault(sort = "modifiedAt", direction = Sort.Direction.DESC)
                                                                           Pageable pageable,
                                                                           @ApiParam(value = "Search by name")
                                                                           @RequestParam(required = false) String query, @RequestParam("x") Double x, @RequestParam("y") Double y) {
        return ResponseEntity.ok(this.userProductService.getAllProduct(pageable, query, x,y));
    }

    @ApiOperation("API for user get top products")
    @GetMapping(path = "top-products")
    public ResponseEntity<Page<UserProductResponseDto>> getAllProductsUser(@PageableDefault(sort = "modifiedAt", direction = Sort.Direction.DESC)
                                                                           Pageable pageable
    ) {
        return ResponseEntity.ok(this.userProductService.getTopProduct(pageable));
    }
}
