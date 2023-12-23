package tech.dut.fasto.web.rest.user.vnpay.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
public class VNPayCreateTokenRequestDto implements Serializable {
    @Serial
    private static final long serialVersionUID = -478645335084050172L;
    private String vnpReturnUrl;
    private String bankCode;
    private String vnpLocale;
}
