package tech.dut.fasto.web.rest.user.bill.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tech.dut.fasto.config.jackson.InstantSerializerCustomizer;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BillResponseDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 8994361536972322232L;
    private Long billId;
    private List<BillProductResponseDto> productResponseDtos;
    private Long code;
    private String typePayment;
    private String statusPayment;
    @JsonSerialize(using = InstantSerializerCustomizer.class)
    private Instant expiredDate;
    private BigDecimal totalOrigin;
    private BigDecimal totalPayment;
    private BigDecimal totalDiscount;
    private BigDecimal shippingFee;
}
