package tech.dut.fasto.web.rest.user.shop;

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
import tech.dut.fasto.config.security.AuthoritiesConstants;
import tech.dut.fasto.web.rest.user.shop.dto.response.UserShopLocationResponseDto;
import tech.dut.fasto.web.rest.user.shop.dto.response.UserShopProfileResponseDto;
import tech.dut.fasto.web.rest.user.shop.dto.response.UserShopResponseDto;
import tech.dut.fasto.web.rest.user.shop.service.UserShopService;

import java.util.List;

@RestController
@RequestMapping("/user/management/shop")
@RequiredArgsConstructor
@Validated
public class UserShopResource {
    private final UserShopService userShopService;

    @ApiOperation("API for user get detail shop")
    @GetMapping(path = "/detail/{shopId}")
    public ResponseEntity<UserShopResponseDto> getDetailShop(@PathVariable("shopId") Long shopId, @RequestParam("x") Double x, @RequestParam("y") Double y) {
        return ResponseEntity.ok(userShopService.getDetailShop(shopId, x,y));
    }


    @ApiOperation("API for user get distance near")
    @GetMapping("/shops")
    public ResponseEntity<Page<UserShopLocationResponseDto>> getDistance(@PageableDefault(sort = "modifiedAt", direction = Sort.Direction.DESC)
                                                                             Pageable pageable, @RequestParam("x") Double x, @RequestParam("y") Double y, @RequestParam(value = "radius" , required = false) Double radius) {
        return ResponseEntity.ok(userShopService.getAllLocationByCurrentLocation(x, y, radius, pageable));
    }

    @ApiOperation("API for shop get distance near")
    @GetMapping("/distance-shop")
    public ResponseEntity<Page<UserShopLocationResponseDto>> getDistanceShop(@RequestParam("shopId") Long shopId, @RequestParam("radius") Double radius, @PageableDefault(sort = "modifiedAt", direction = Sort.Direction.DESC)
    Pageable pageable) {
        return ResponseEntity.ok(userShopService.getAllShopLocationByCurrentLocation(shopId, radius, pageable));
    }

//    @ApiOperation("API for get all shops")
//    @GetMapping(path = "/shops")
//    @SwaggerPageable
//    public ResponseEntity<Page<UserShopProfileResponseDto>> getAllShop(@PageableDefault(sort = "modifiedAt", direction = Sort.Direction.DESC)
//                                                                       Pageable pageable,
//                                                                       @RequestParam(required = false) String query) {
//        return ResponseEntity.ok(userShopService.getAllShops(query, pageable));
//    }

    @ApiOperation("API for create recent shop")
    @PostMapping(path = "/create/recent-shop")
    @PreAuthorize("hasAuthority('" + AuthoritiesConstants.USER + "')")
    public ResponseEntity<?> createRecentShop(@ApiParam(value = "ShopId") @RequestParam(required = true) Long shopId) {
        userShopService.createRecentShop(shopId);
        return ResponseEntity.ok().build();
    }

    @ApiOperation("API for get all recent shop")
    @GetMapping(path = "/recent-shops")
    @SwaggerPageable
    @PreAuthorize("hasAuthority('" + AuthoritiesConstants.USER + "')")
    public ResponseEntity<Page<UserShopProfileResponseDto>> getAllRecentShop(@PageableDefault(sort = "modifiedAt", direction = Sort.Direction.DESC)
                                                                             Pageable pageable) {
        return ResponseEntity.ok(userShopService.getRecentShop(pageable));
    }

    @ApiOperation("API for get top shop")
    @GetMapping(path = "/top-shops")
    @SwaggerPageable
    public ResponseEntity<Page<UserShopProfileResponseDto>> getTopShop(@PageableDefault(sort = "modifiedAt", direction = Sort.Direction.DESC)
                                                                             Pageable pageable) {
        return ResponseEntity.ok(userShopService.getTopShops(pageable));
    }
}
