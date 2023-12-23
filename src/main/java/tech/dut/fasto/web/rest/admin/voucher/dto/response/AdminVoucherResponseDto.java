package tech.dut.fasto.web.rest.admin.voucher.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tech.dut.fasto.common.domain.enumeration.VoucherProvider;
import tech.dut.fasto.common.domain.enumeration.VoucherStatus;
import tech.dut.fasto.common.domain.enumeration.VoucherType;
import tech.dut.fasto.config.jackson.InstantSerializerCustomizer;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminVoucherResponseDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1514919387041875742L;
    private Long id;

    private String name;

    private Long countUserVoucher;

    @JsonSerialize(using = InstantSerializerCustomizer.class)
    private Instant startedAt;

    @JsonSerialize(using = InstantSerializerCustomizer.class)
    private Instant endedAt;
    private VoucherProvider userType;
    private BigDecimal valueDiscount;
    private VoucherType voucherType;
    private BigDecimal valueNeed;
    private Long limitPerUser;
    private BigDecimal maxDiscount;
    private Long quantity;

    private VoucherStatus status;

    private String image;

    private String description;


    public AdminVoucherResponseDto(Long id, String name, Instant startedAt, Instant endedAt, VoucherProvider userType, BigDecimal valueDiscount, VoucherType voucherType, BigDecimal valueNeed, Long limitPerUser, BigDecimal maxDiscount, Long quantity, VoucherStatus status, String image, String description) {
        this.id = id;
        this.name = name;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.userType = userType;
        this.valueDiscount = valueDiscount;
        this.voucherType = voucherType;
        this.valueNeed = valueNeed;
        this.limitPerUser = limitPerUser;
        this.maxDiscount = maxDiscount;
        this.quantity = quantity;
        this.status = status;
        this.image = image;
        this.description = description;
    }

}