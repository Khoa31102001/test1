package tech.dut.fasto.web.rest.shop.vnpay.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
public class VNPayCreateUrlRequestShopDto implements Serializable {


    @Serial
    private static final long serialVersionUID = -157370429801002344L;
    private String vnpReturnUrl;
    private String bankCode;
    private String vnpLocale;
    private String vnpOrderType;
}
