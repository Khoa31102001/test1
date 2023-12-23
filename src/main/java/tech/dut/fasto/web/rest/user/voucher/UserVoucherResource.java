package tech.dut.fasto.web.rest.user.voucher;

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
import tech.dut.fasto.common.domain.enumeration.VoucherProvider;
import tech.dut.fasto.config.security.AuthoritiesConstants;
import tech.dut.fasto.web.rest.user.voucher.dto.request.UserVoucherProductRequestDto;
import tech.dut.fasto.web.rest.user.voucher.dto.response.VoucherUserResponseDto;
import tech.dut.fasto.web.rest.user.voucher.service.UserVoucherService;

import java.util.List;

@RestController
@RequestMapping("/user/management/vouchers")
@RequiredArgsConstructor
@Validated
public class UserVoucherResource {
    private final UserVoucherService userVoucherService;

    @ApiOperation("API for user get all voucher for bill")
    @PostMapping("/bill")
    @PreAuthorize("hasAuthority('" + AuthoritiesConstants.USER + "')")
    public ResponseEntity<List<VoucherUserResponseDto>> getAllVouchersForBill(@RequestBody UserVoucherProductRequestDto voucherProductRequestDto) {
        return ResponseEntity.ok(userVoucherService.getAllVoucherForBill(voucherProductRequestDto));
    }

    @ApiOperation("API for user get detail voucher shop")
    @GetMapping("/{id}")
    public ResponseEntity<VoucherUserResponseDto> getDetail(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userVoucherService.getDetailVoucher(id));
    }

    @ApiOperation("API for get all voucher")
    @GetMapping
    @SwaggerPageable
    public ResponseEntity<Page<VoucherUserResponseDto>> getAllVoucher(@PageableDefault(sort = "modifiedAt", direction = Sort.Direction.DESC)
                                                                      Pageable pageable,
                                                                      @ApiParam(value = "Search by name")
                                                                      @RequestParam(required = false) String query,
                                                                      @RequestParam(required = false) VoucherProvider voucherProvider) {
        return ResponseEntity.ok(userVoucherService.getAllVoucher(voucherProvider, pageable, query));
    }
}
