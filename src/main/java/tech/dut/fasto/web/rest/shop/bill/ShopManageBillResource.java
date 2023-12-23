package tech.dut.fasto.web.rest.shop.bill;

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
import tech.dut.fasto.common.domain.enumeration.BillStatus;
import tech.dut.fasto.config.security.AuthoritiesConstants;
import tech.dut.fasto.web.rest.shop.bill.dto.response.BillCodeResponseDto;
import tech.dut.fasto.web.rest.shop.bill.dto.response.BillDetailResponseDto;
import tech.dut.fasto.web.rest.shop.bill.dto.response.BillUserResponseDto;
import tech.dut.fasto.web.rest.shop.bill.service.ShopBillService;

@RestController
@RequestMapping("/shop/management")
@RequiredArgsConstructor
@Validated
@PreAuthorize("hasAuthority('" + AuthoritiesConstants.SHOP + "')")
public class ShopManageBillResource {

    private final ShopBillService shopBillService;

    @ApiOperation("API for shop all list bill")
    @GetMapping(path = "/bills")
    public ResponseEntity<Page<BillUserResponseDto>> getAllListBill(@PageableDefault(sort = "modifiedAt", direction = Sort.Direction.DESC)
                                                                    Pageable pageable, @RequestParam BillStatus status, @ApiParam(value = "Search by name")
                                                                    @RequestParam(required = false) String query) {
        return ResponseEntity.ok().body(shopBillService.getAllListBill(status, query, pageable));
    }

    @ApiOperation("API for user shop get detail bill")
    @GetMapping("/bill/{id}")
    public ResponseEntity<BillDetailResponseDto> getDetailBill(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(shopBillService.getDetailBill(id));
    }

//    @ApiOperation("API for user shop get detail bill by code")
//    @GetMapping("/bill/code")
//    public ResponseEntity<BillDetailResponseDto> getBillByCode(@RequestParam("code") String code) {
//        return ResponseEntity.ok().body(shopBillService.getBillByCode(code));
//    }

//    @ApiOperation("API for user shop get detail bill by code")
//    @GetMapping("/bill/code/{id}")
//    public ResponseEntity<BillCodeResponseDto> getCode(@PathVariable("id") Long billId) {
//        return ResponseEntity.ok().body(shopBillService.getCodeBill(billId));
//    }
//
    @ApiOperation("API for user shop get detail bill by code")
    @PatchMapping("/bill/{id}/valid-code")
    public ResponseEntity<BillCodeResponseDto> getCode(@PathVariable("id") Long id) {
        shopBillService.validCodeForBill(id);
        return ResponseEntity.ok().build();
    }
}
