package tech.dut.fasto.web.rest.user.vnpay.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
public class VNPayCreateTokenPaymentUrlRequestDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1836024127747546711L;
    private String billId;
    private String vnpReturnUrl;
    private String vnpLocale;
    private String vnpOrderType;
    private String cardNumber;
}
