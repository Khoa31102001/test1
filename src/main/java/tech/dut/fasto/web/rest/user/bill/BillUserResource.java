package tech.dut.fasto.web.rest.user.bill;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tech.dut.fasto.common.domain.enumeration.BillStatus;
import tech.dut.fasto.config.security.AuthoritiesConstants;
import tech.dut.fasto.web.rest.shop.bill.dto.response.BillDetailResponseDto;
import tech.dut.fasto.web.rest.user.bill.dto.request.BillProductRequestDto;
import tech.dut.fasto.web.rest.user.bill.dto.response.BillResponseDto;
import tech.dut.fasto.web.rest.user.bill.dto.response.ShippingFeeResponseDto;
import tech.dut.fasto.web.rest.user.bill.dto.response.UserBillResponseDto;
import tech.dut.fasto.web.rest.user.bill.service.UserBillService;

import java.util.List;

@RestController
@RequestMapping("/user/management/bill")
@RequiredArgsConstructor
@Validated
@PreAuthorize("hasAuthority('" + AuthoritiesConstants.USER + "')")
public class BillUserResource {
    private final UserBillService userBillService;

    @ApiOperation("API for user create bill")
    @PostMapping(path ="/create")
    public ResponseEntity<BillResponseDto> createBill(@RequestBody BillProductRequestDto billProductRequestDto) {
        return ResponseEntity.ok(userBillService.createBill(billProductRequestDto));
    }

    @ApiOperation("API for user all list bill")
    @GetMapping
    public ResponseEntity<List<UserBillResponseDto>> getAllListBill(@RequestParam BillStatus status, @ApiParam(value = "Search by name")
    @RequestParam(required = false) String query) {
        return ResponseEntity.ok(userBillService.getAllListBill(status, query));
    }

    @ApiOperation("API for user all get detail bill")
    @GetMapping("/{id}")
    public ResponseEntity<BillDetailResponseDto> getDetailBill(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userBillService.getDetailBill(id));
    }
    @ApiOperation("API for user all get detail bill")
    @GetMapping("/payment/{id}")
    public ResponseEntity<String> proceedPayment(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userBillService.proceedPayment(id));
    }

    @ApiOperation("API for done bill")
    @GetMapping("/done/{id}")
    public ResponseEntity<String> doneBill(@PathVariable("id") Long id) {
        userBillService.doneBill(id);
        return ResponseEntity.ok().build();
    }

    @ApiOperation("Calculate shipping fee")
    @GetMapping("/shipping/{shopId}")
    public ResponseEntity<ShippingFeeResponseDto> shippingFee(@PathVariable("shopId") Long id, @RequestParam Double x, @RequestParam Double y ) {

        return ResponseEntity.ok(userBillService.calculateShippingFee(id, x, y));
    }
}
