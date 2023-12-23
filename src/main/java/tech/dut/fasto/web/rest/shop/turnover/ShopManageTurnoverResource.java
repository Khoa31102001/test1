package tech.dut.fasto.web.rest.shop.turnover;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
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
import tech.dut.fasto.config.security.AuthoritiesConstants;
import tech.dut.fasto.web.rest.shop.turnover.dto.response.TopProductResponseDto;
import tech.dut.fasto.web.rest.shop.turnover.dto.response.TurnoverResponseDto;
import tech.dut.fasto.web.rest.shop.turnover.service.ShopTurnoverService;

import java.util.List;

@RestController
@RequestMapping("/shop/management/revenue")
@RequiredArgsConstructor
@Validated
@PreAuthorize("hasAuthority('" + AuthoritiesConstants.SHOP + "')")
public class ShopManageTurnoverResource {

    private final ShopTurnoverService shopTurnoverService;

    @ApiOperation("API for Shop turnover Revenue")
    @GetMapping
    public ResponseEntity<TurnoverResponseDto> getRevenue(@RequestParam("dateFrom") long dateFrom, @RequestParam("dateTo") Long dateTo) {
        return ResponseEntity.ok().body(this.shopTurnoverService.getRevenue(dateFrom, dateTo));
    }

    @ApiOperation("API for Shop turnover product")
    @GetMapping("/product")
    public ResponseEntity<List<TopProductResponseDto>> getTopProduct(@RequestParam("dateFrom") Long dateFrom, @RequestParam("dateTo") Long dateTo, @PageableDefault(direction = Sort.Direction.DESC)
    Pageable pageable) {
        return ResponseEntity.ok().body(shopTurnoverService.getTopProduct(pageable, dateFrom, dateTo));
    }
}
