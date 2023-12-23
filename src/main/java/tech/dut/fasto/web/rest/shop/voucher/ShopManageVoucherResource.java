package tech.dut.fasto.web.rest.shop.voucher;

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
import tech.dut.fasto.common.domain.enumeration.VoucherStatus;
import tech.dut.fasto.config.security.AuthoritiesConstants;
import tech.dut.fasto.web.rest.shop.voucher.dto.request.ShopVoucherRequestDto;
import tech.dut.fasto.web.rest.shop.voucher.dto.response.ShopVoucherResponseDto;
import tech.dut.fasto.web.rest.shop.voucher.service.ShopVoucherService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/shop/management")
@RequiredArgsConstructor
@Validated
@PreAuthorize("hasAuthority('" + AuthoritiesConstants.SHOP + "')")
public class ShopManageVoucherResource {

    private final ShopVoucherService shopVoucherService;
    @ApiOperation("API for Shop add voucher")
    @PostMapping("/voucher")
    public ResponseEntity<?> createVoucher(@Valid @RequestBody ShopVoucherRequestDto shopVoucherRequestDto) {
        shopVoucherService.createVoucher(shopVoucherRequestDto);
        return ResponseEntity.ok().build();
    }

    @ApiOperation("API for get all vouchers")
    @GetMapping(path = "/vouchers")
    public ResponseEntity<Page<ShopVoucherResponseDto>> getAllVouchers(@PageableDefault(sort = "modifiedAt", direction = Sort.Direction.DESC)
    Pageable pageable,
                                                                       @ApiParam(value = "Search by name")
                                                                @RequestParam(required = false) String query,
                                                                       @ApiParam(value = "Search by status")
                                                                @RequestParam(required = false) VoucherStatus status) {
        return ResponseEntity.ok().body(this.shopVoucherService.getAllVoucher(pageable, query, status));
    }

    @ApiOperation("API for Shop delete voucher")
    @DeleteMapping("/voucher/delete/{id}")
    public ResponseEntity<?> deleteVoucher(@NotNull @PathVariable("id") Long id) {
        this.shopVoucherService.deleteVoucher(id);
        return ResponseEntity.ok().build();
    }

    @ApiOperation("API for Shop update voucher")
    @PutMapping("/voucher/update/{id}")
    public ResponseEntity<?> updateVoucher(@NotNull @PathVariable("id") Long id, @Valid @RequestBody ShopVoucherRequestDto shopVoucherRequestDto) {
        shopVoucherService.updateVoucher(shopVoucherRequestDto,id);
        return ResponseEntity.ok().build();
    }

    @ApiOperation("API for Shop get detail voucher")
    @GetMapping("/voucher/detail/{id}")
    public ResponseEntity<ShopVoucherResponseDto> getDetailVoucher(@NotNull @PathVariable("id") Long id) {
        return ResponseEntity.ok().body(shopVoucherService.getDetail(id));
    }
}
