package tech.dut.fasto.web.rest.shop.bill.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tech.dut.fasto.common.domain.enumeration.BillStatus;
import tech.dut.fasto.web.rest.shop.bill.dto.response.BillCodeResponseDto;
import tech.dut.fasto.web.rest.shop.bill.dto.response.BillDetailResponseDto;
import tech.dut.fasto.web.rest.shop.bill.dto.response.BillUserResponseDto;

public interface ShopBillService {

     Page<BillUserResponseDto> getAllListBill(BillStatus status, String query, Pageable pageable);

     BillDetailResponseDto getDetailBill(Long billId);

//     BillDetailResponseDto getBillByCode(String code);

//     BillCodeResponseDto getCodeBill(Long billId);
//
     void validCodeForBill(Long id);
}
