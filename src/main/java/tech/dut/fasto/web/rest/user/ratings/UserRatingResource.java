package tech.dut.fasto.web.rest.user.ratings;

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
import tech.dut.fasto.web.rest.shop.rating.dto.response.ShopRatingResponseDto;
import tech.dut.fasto.web.rest.user.ratings.dto.request.UserFeedBackShopRequestDto;
import tech.dut.fasto.web.rest.user.ratings.service.UserRatingService;

@RestController
@RequestMapping("/user/management/rating")
@RequiredArgsConstructor
@Validated

public class UserRatingResource {
    private final UserRatingService userRatingService;

    @ApiOperation("API for user feedback shop")
    @PostMapping
    @PreAuthorize("hasAuthority('" + AuthoritiesConstants.USER + "')")
    public ResponseEntity<?> feedBackShop(@RequestBody UserFeedBackShopRequestDto userFeedBackShopRequestDto) {
        userRatingService.feedBackStart(userFeedBackShopRequestDto);
        return ResponseEntity.ok().build();
    }

    @ApiOperation("API for user get all ratings shop")
    @GetMapping("/{shopId}")
    @SwaggerPageable
    public ResponseEntity<Page<ShopRatingResponseDto>> getAllRatings(@PathVariable("shopId") Long shopId, @PageableDefault(sort = "modifiedAt", direction = Sort.Direction.DESC)
    Pageable pageable) {
        return ResponseEntity.ok(userRatingService.getAllRatingUser(shopId,pageable));
    }
}
