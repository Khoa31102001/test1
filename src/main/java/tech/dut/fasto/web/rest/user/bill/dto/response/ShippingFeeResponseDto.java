package tech.dut.fasto.web.rest.user.bill.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
public class ShippingFeeResponseDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 2456069417840950784L;
    private BigDecimal ShippingFee;
}
