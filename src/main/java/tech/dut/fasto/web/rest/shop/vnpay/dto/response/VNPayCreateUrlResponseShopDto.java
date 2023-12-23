package tech.dut.fasto.web.rest.shop.vnpay.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VNPayCreateUrlResponseShopDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 3952360154449403941L;
    private String url;
    private Long billId;
}
