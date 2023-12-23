package tech.dut.fasto.web.rest.shop.voucher.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tech.dut.fasto.common.domain.enumeration.VoucherStatus;
import tech.dut.fasto.common.domain.enumeration.VoucherType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShopVoucherRequestDto implements Serializable {
    @Serial
    private static final long serialVersionUID = -7950366868280058518L;

    private Long id;
    @Size(max = 50 , message = "validate.error.size")
    @NotBlank(message = "validate.not.empty")
    private  String name;
    @NotNull(message = "validate.not.empty")
    private Long endedAt;
    @NotNull(message = "validate.not.empty")
    private  BigDecimal valueDiscount;
    @NotNull(message = "validate.not.empty")
    private VoucherType voucherType;
    @NotNull(message = "validate.not.empty")
    private BigDecimal valueNeed;
    @NotNull(message = "validate.not.empty")
    private  Long limitPerUser;
    @NotNull(message = "validate.not.empty")
    private  BigDecimal maxDiscount;
    @NotNull(message = "validate.not.empty")
    private Long quantity;

    @NotBlank(message = "validate.not.empty")
    private String image;

    @NotNull(message = "validate.not.empty")
    private Long startedAt;

    @NotNull(message = "validate.not.empty")
    private VoucherStatus status;

    private String description;
}
