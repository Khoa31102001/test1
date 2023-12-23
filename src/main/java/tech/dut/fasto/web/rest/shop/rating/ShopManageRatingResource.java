package tech.dut.fasto.web.rest.shop.rating;

import io.swagger.annotations.ApiOperation;
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
import org.springframework.web.bind.annotation.RestController;
import tech.dut.fasto.common.anotation.SwaggerPageable;
import tech.dut.fasto.config.security.AuthoritiesConstants;
import tech.dut.fasto.web.rest.shop.rating.dto.response.ShopRatingResponseDto;
import tech.dut.fasto.web.rest.shop.rating.service.ShopRatingService;

@RestController
@RequestMapping("/shop/management")
@RequiredArgsConstructor
@Validated
@PreAuthorize("hasAuthority('" + AuthoritiesConstants.SHOP + "')")
public class ShopManageRatingResource {

    private final ShopRatingService shopRatingService;

    @ApiOperation("API for get all products")
    @GetMapping(path = "/ratings")
    @SwaggerPageable
    public ResponseEntity<Page<ShopRatingResponseDto>> getAllProducts(@PageableDefault(sort = "modifiedAt", direction = Sort.Direction.DESC)
                                                                      Pageable pageable) {
        return ResponseEntity.ok().body(this.shopRatingService.getAllRatings(pageable));
    }

}
