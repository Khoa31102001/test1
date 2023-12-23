package tech.dut.fasto.web.rest.admin.voucher;

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
import tech.dut.fasto.web.rest.admin.voucher.dto.request.AdminVoucherRequestDto;
import tech.dut.fasto.web.rest.admin.voucher.dto.response.AdminVoucherResponseDto;
import tech.dut.fasto.web.rest.admin.voucher.service.AdminVoucherService;
import tech.dut.fasto.web.rest.shop.voucher.dto.response.ShopVoucherResponseDto;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/management")
@RequiredArgsConstructor
@Validated
@PreAuthorize("hasAuthority('" + AuthoritiesConstants.ADMIN + "')")
public class AdminVoucherResource {
    private final AdminVoucherService adminVoucherService;

    @ApiOperation("API for admin create voucher")
    @PostMapping(path = "/voucher")
    public ResponseEntity<?> createVoucher(@RequestBody @Valid AdminVoucherRequestDto voucherRequestDto) {
        adminVoucherService.createVoucher(voucherRequestDto);
        return ResponseEntity.ok().build();
    }

    @ApiOperation("API for admin delete voucher")
    @DeleteMapping(path = "/voucher")
    public ResponseEntity<?> deleteVoucher(@PathVariable("id") Long id) {
        adminVoucherService.deleteVoucher(id);
        return ResponseEntity.ok().build();
    }

    @ApiOperation("API for admin update voucher")
    @PutMapping(path = "/vouchers/update/{id}")
    public ResponseEntity<?> updateVoucher(@PathVariable("id") Long id, @RequestBody @Valid AdminVoucherRequestDto voucherRequestDto) {
        adminVoucherService.updateVoucher(voucherRequestDto,id);
        return ResponseEntity.ok().build();
    }

    @ApiOperation("API for admin get detail shop voucher")
    @GetMapping(path = "/voucher/{id}")
    public ResponseEntity<AdminVoucherResponseDto> getDetailVoucher(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(adminVoucherService.getDetailVoucher(id));
    }

    @ApiOperation("API for admin get  detail shop voucher")
    @GetMapping(path = "/vouchers/shops/detail/{id}")
    public ResponseEntity<ShopVoucherResponseDto> getDetailVoucherShop(@PathVariable("id") Long id) {
        return  ResponseEntity.ok().body(adminVoucherService.getDetailVoucherShop(id));
    }

    @ApiOperation("API for get all vouchers of Shop")
    @GetMapping(path = "/vouchers/shops/{id}")
    public ResponseEntity<Page<ShopVoucherResponseDto>> getAllVouchersShop(@PageableDefault(sort = "modifiedAt", direction = Sort.Direction.DESC)
                                                                               Pageable pageable,
                                                                           @PathVariable("id") Long id,
                                                                    @ApiParam(value = "Search by name")
                                                                    @RequestParam(required = false) String query,
                                                                    @ApiParam(value = "Search by status")
                                                                    @RequestParam(required = false) VoucherStatus status) {
        return ResponseEntity.ok().body(adminVoucherService.getAllVoucherShop(pageable,id, query, status));
    }

    @ApiOperation("API for get all vouchers admin")
    @GetMapping(path = "/vouchers")
    public ResponseEntity<Page<AdminVoucherResponseDto>> getAllVouchers(@PageableDefault(sort = "modifiedAt", direction = Sort.Direction.DESC)
                                                                        Pageable pageable,  @ApiParam(value = "Search by name")
                                                                    @RequestParam(required = false) String query,
                                                                    @ApiParam(value = "Search by status")
                                                                    @RequestParam(required = false) VoucherStatus status) {
        return ResponseEntity.ok().body(adminVoucherService.getAllVoucher(pageable, query, status));
    }
}
