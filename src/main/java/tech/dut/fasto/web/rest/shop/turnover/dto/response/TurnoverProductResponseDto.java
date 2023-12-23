package tech.dut.fasto.web.rest.shop.turnover.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TurnoverProductResponseDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 4609513048627002161L;

    Long id;
    String productName;
    Long quantity;
    BigDecimal totalOriginPrice;
}
