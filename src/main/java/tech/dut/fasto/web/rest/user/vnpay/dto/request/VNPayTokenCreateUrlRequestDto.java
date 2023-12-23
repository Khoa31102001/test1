package tech.dut.fasto.web.rest.user.vnpay.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
public class VNPayTokenCreateUrlRequestDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 53837844810988918L;
    private String billId;
    private String vnpReturnUrl;
    private String bankCode;
    private String vnpLocale;
    private String vnpOrderType;
    private String cardNumber;
}
