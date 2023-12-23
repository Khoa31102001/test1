package tech.dut.fasto.web.rest.user.voucher.dto.response;

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
public class VoucherUserResponseDto implements Serializable {
    @Serial
    private static final long serialVersionUID = -5096477340028040357L;
    private Long id;

    private String name;

    @JsonSerialize(using = InstantSerializerCustomizer.class)
    private Instant startedAt;
    @JsonSerialize(using = InstantSerializerCustomizer.class)
    private Instant endedAt;

    private VoucherProvider userType;

    private String image;

    private BigDecimal valueDiscount;

    private VoucherType voucherType;

    private BigDecimal valueNeed;

    private Long limitPerUser;

    private BigDecimal maxDiscount;

    private Long quantity;

    private Long shopId;

    private String shopName;

    private String description;


    private VoucherStatus status;

    private Long countVoucherUsed;

    public VoucherUserResponseDto(Long id, String name, Instant createdAt, Instant endedAt, VoucherProvider userType, String image, BigDecimal valueDiscount, VoucherType voucherType, BigDecimal valueNeed, Long limitPerUser, BigDecimal maxDiscount, Long quantity, Long shopId, String shopName, String description) {
        this.id = id;
        this.name = name;
        this.startedAt = createdAt;
        this.endedAt = endedAt;
        this.userType = userType;
        this.image = image;
        this.valueDiscount = valueDiscount;
        this.voucherType = voucherType;
        this.valueNeed = valueNeed;
        this.limitPerUser = limitPerUser;
        this.maxDiscount = maxDiscount;
        this.quantity = quantity;
        this.shopId = shopId;
        this.shopName = shopName;
        this.description = description;
    }

    public VoucherUserResponseDto(Long id, String name, Instant createdAt, Instant endedAt, VoucherProvider userType, String image, BigDecimal valueDiscount, VoucherType voucherType, BigDecimal valueNeed, Long limitPerUser, BigDecimal maxDiscount, Long quantity, Long shopId, String shopName, String description, Long countVoucherUsed) {
        this.id = id;
        this.name = name;
        this.startedAt = createdAt;
        this.endedAt = endedAt;
        this.userType = userType;
        this.image = image;
        this.valueDiscount = valueDiscount;
        this.voucherType = voucherType;
        this.valueNeed = valueNeed;
        this.limitPerUser = limitPerUser;
        this.maxDiscount = maxDiscount;
        this.quantity = quantity;
        this.shopId = shopId;
        this.shopName = shopName;
        this.description = description;
        this.countVoucherUsed = countVoucherUsed;
    }
}
