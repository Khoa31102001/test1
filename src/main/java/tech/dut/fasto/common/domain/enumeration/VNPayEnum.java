package tech.dut.fasto.common.domain.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class VNPayEnum {
    @Getter
    @AllArgsConstructor
    public enum PaymentStatus {
        SUCCESS("00", "Thanh toan thanh cong"),
        UNKNOW_ERR("99", "Loi khong xac dinh"),
        INVALID_CHECKSUM("97", "Chu ki khong hop le"),
        ALREADY_CONFIRM("02", "Order da duoc thanh toan"),
        INVALID_AMOUNT("04", "So tien khong hop le"),
        ORDER_NOT_FOUND("01", "Order khong tim thay"),

        CANCEL_PAYMENT("24", "Huy thanh toan thanh cong");

        private final String code;
        private final String message;
    }

    @Getter
    @AllArgsConstructor
    public enum TokenStatus {
        SUCCESS("00", "Tao token thanh cong"),
        ALREADY_TRANSACTION("01", "Giao dich da ton tai"),
        MERCHANT_INVALID("02", "Merchant Khong hop le"),
        MALFORMED_DATA("03", "Du lieu gui sang khong dung dinh dang"),
        TRANSACTION_INIT_FAILED("04", "Website dang bi tam khoa"),
        TRANSACTION_FAILED("08", "Giao dich khong thanh cong"),
        TRANSACTION_CANCELLED("24", "Giao dich bi huy"),
        UNKNOW_ERR("99", "Loi khong xac dinh"),
        INVALID_CHECKSUM("97", "Chu ki khong hop le");

        private final String code;
        private final String message;
    }

    @Getter
    @AllArgsConstructor
    public enum VNPayServiceType {
        TOKEN_CREATE("token_create"),
        PAY_AND_CREATE("pay_and_create"),
        TOKEN_PAY("token_pay");
        private final String type;

    }
}
