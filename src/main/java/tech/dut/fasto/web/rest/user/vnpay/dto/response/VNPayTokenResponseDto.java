package tech.dut.fasto.web.rest.user.vnpay.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
public class VNPayTokenResponseDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 5839112630351849858L;
    private String url;

}
