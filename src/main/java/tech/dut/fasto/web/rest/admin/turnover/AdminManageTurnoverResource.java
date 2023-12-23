package tech.dut.fasto.web.rest.admin.turnover;

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
import tech.dut.fasto.config.security.AuthoritiesConstants;
import tech.dut.fasto.web.rest.admin.turnover.dto.response.AdminTurnoverTotalShopResponseDto;
import tech.dut.fasto.web.rest.admin.turnover.service.AdminTurnoverService;

@RestController
@RequestMapping("/admin/management/turnover")
@RequiredArgsConstructor
@Validated
@PreAuthorize("hasAuthority('" + AuthoritiesConstants.ADMIN + "')")
public class AdminManageTurnoverResource {
    private final AdminTurnoverService adminTurnoverService;

    @ApiOperation("API for Admin turnover Revenue Shop")
    @GetMapping("/shops")
    public ResponseEntity<Page<AdminTurnoverTotalShopResponseDto>> getRevenue(@PageableDefault(sort = "s.id", direction = Sort.Direction.DESC)
                                                                            Pageable pageable, @RequestParam("dateFrom") Long dateFrom, @RequestParam("dateTo") Long dateTo) {
        return ResponseEntity.ok(adminTurnoverService.getRevenue(dateFrom, dateTo, pageable));
    }


    @ApiOperation("API for Admin turnover Revenue Shop")
    @GetMapping("/shops/{shopId}")
    public ResponseEntity<AdminTurnoverTotalShopResponseDto> getRevenue(@RequestParam("dateFrom") Long dateFrom, @RequestParam("dateTo") Long dateTo, @PathVariable("shopId") Long shopId) {
        return ResponseEntity.ok(adminTurnoverService.getRevenueShop(dateFrom, dateTo, shopId));
    }

    @ApiOperation("API for Admin turnover Revenue all")
    @GetMapping("/shops/all")
    public ResponseEntity<AdminTurnoverTotalShopResponseDto> getAllRevenue(@RequestParam("dateFrom") Long dateFrom, @RequestParam("dateTo") Long dateTo, @PathVariable("shopId") Long shopId) {
        return ResponseEntity.ok(adminTurnoverService.getAllRevenue(dateFrom, dateTo));
    }
}
