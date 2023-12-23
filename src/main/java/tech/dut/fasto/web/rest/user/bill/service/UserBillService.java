package tech.dut.fasto.web.rest.user.bill.service;

import tech.dut.fasto.common.domain.enumeration.BillStatus;
import tech.dut.fasto.web.rest.shop.bill.dto.response.BillDetailResponseDto;
import tech.dut.fasto.web.rest.user.bill.dto.request.BillProductRequestDto;
import tech.dut.fasto.web.rest.user.bill.dto.response.BillResponseDto;
import tech.dut.fasto.web.rest.user.bill.dto.response.ShippingFeeResponseDto;
import tech.dut.fasto.web.rest.user.bill.dto.response.UserBillResponseDto;

import java.util.List;

public interface UserBillService {
    BillResponseDto createBill(BillProductRequestDto billProductRequestDto);

    List<UserBillResponseDto> getAllListBill(BillStatus status, String query);

    BillDetailResponseDto getDetailBill(Long billId);
    String proceedPayment(Long id);

    void doneBill(Long id);

    ShippingFeeResponseDto calculateShippingFee(Long shopId, Double x, Double y);
}
