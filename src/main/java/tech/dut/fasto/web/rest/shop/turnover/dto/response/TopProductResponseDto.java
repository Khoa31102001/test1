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
public class TopProductResponseDto implements Serializable {
    @Serial
    private static final long serialVersionUID = -4103130344587958848L;

    Long id;
    String productName;
    Long quantity;
    String image;
    BigDecimal price;
}
