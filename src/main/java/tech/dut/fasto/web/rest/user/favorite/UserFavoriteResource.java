package tech.dut.fasto.web.rest.user.favorite;

import io.swagger.annotations.ApiOperation;
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
import tech.dut.fasto.web.rest.user.favorite.dto.response.ShopFavoriteResponseDto;
import tech.dut.fasto.web.rest.user.favorite.service.UserFavoriteService;

@RestController
@RequestMapping("/user/management")
@RequiredArgsConstructor
@Validated
@PreAuthorize("hasAuthority('" + AuthoritiesConstants.USER + "')")
public class UserFavoriteResource {
    private final UserFavoriteService userFavoriteService;

    @ApiOperation("API for user get all favourite shop")
    @GetMapping(path = "/shops")
    @SwaggerPageable
    public ResponseEntity<Page<ShopFavoriteResponseDto>> getAllShopFavorites(@PageableDefault(sort = "modifiedAt", direction = Sort.Direction.DESC)
                                                                             Pageable pageable) {
        return ResponseEntity.ok(userFavoriteService.getAllFavouriteUsers(pageable));
    }

    @ApiOperation("API for user add favourite shop")
    @PostMapping(path = "/shops/{shopId}")
    public ResponseEntity<?> createShopFavorite(@PathVariable("shopId") Long shopId) {
        userFavoriteService.createFavouriteShop(shopId);
        return ResponseEntity.ok().build();
    }

    @ApiOperation("API for user delete favourite shop")
    @DeleteMapping(path = "/shops/{shopId}")
    public ResponseEntity<?> deleteShopFavorite(@PathVariable("shopId") Long shopId) {
        userFavoriteService.deleteFavouriteShop(shopId);
        return ResponseEntity.ok().build();
    }

}
