package tech.dut.fasto.web.rest.shop.voucher.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
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
public class ShopVoucherResponseDto implements Serializable {
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


    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long shopId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String shopName;

    private String description;

    public ShopVoucherResponseDto(Long id, String name, Instant startedAt, Instant endedAt, VoucherProvider userType, BigDecimal valueDiscount, VoucherType voucherType, BigDecimal valueNeed, Long limitPerUser, BigDecimal maxDiscount, Long quantity, VoucherStatus status, String image, String description, Long shopId, String shopName) {
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
        this.shopId = shopId;
        this.shopName = shopName;
    }

    public ShopVoucherResponseDto(Long id, String name, Instant startedAt, Instant endedAt, VoucherProvider userType, BigDecimal valueDiscount, VoucherType voucherType, BigDecimal valueNeed, Long limitPerUser, BigDecimal maxDiscount, Long quantity, VoucherStatus status, String image, String description, Long shopId, String shopName, Long countUserVoucher) {
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
        this.shopId = shopId;
        this.shopName = shopName;
        this.countUserVoucher = countUserVoucher;
    }

    public ShopVoucherResponseDto(Long id, String name, Instant startedAt, Instant endedAt, VoucherProvider userType, BigDecimal valueDiscount, VoucherType voucherType, BigDecimal valueNeed, Long limitPerUser, BigDecimal maxDiscount, Long quantity, String image, String description, Long shopId, String shopName) {
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
        this.image = image;
        this.description = description;
        this.shopId = shopId;
        this.shopName = shopName;
    }

    public ShopVoucherResponseDto(Long id, String name, Instant startedAt, Instant endedAt, VoucherProvider userType, BigDecimal valueDiscount, VoucherType voucherType, BigDecimal valueNeed, Long limitPerUser, BigDecimal maxDiscount, Long quantity, String image, String description, Long shopId, String shopName, Long countVoucher, VoucherStatus status) {
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
        this.image = image;
        this.description = description;
        this.shopId = shopId;
        this.shopName = shopName;
        this.countUserVoucher = countVoucher;
        this.status = status;
    }

    public ShopVoucherResponseDto(Long id, String name, Instant startedAt, Instant endedAt, VoucherProvider userType, BigDecimal valueDiscount, VoucherType voucherType, BigDecimal valueNeed, Long limitPerUser, BigDecimal maxDiscount, Long quantity, String image, String description, Long countVoucher, VoucherStatus status) {
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
        this.image = image;
        this.description = description;
        this.countUserVoucher = countVoucher;
        this.status = status;
    }

    public ShopVoucherResponseDto(Long id, String name, Instant startedAt, Instant endedAt, VoucherProvider userType, BigDecimal valueDiscount, VoucherType voucherType, BigDecimal valueNeed, Long limitPerUser, BigDecimal maxDiscount, Long quantity, String image, String description, VoucherStatus status, Long shopId) {
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
        this.image = image;
        this.description = description;
        this.status = status;
        this.shopId = shopId;
    }

    public ShopVoucherResponseDto(Long id, String name, Instant startedAt, Instant endedAt, VoucherProvider userType, BigDecimal valueDiscount, VoucherType voucherType, BigDecimal valueNeed, Long limitPerUser, BigDecimal maxDiscount, Long quantity, VoucherStatus status, String image, String description) {
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

    public ShopVoucherResponseDto(Long id, String name, Instant startedAt, Instant endedAt, VoucherProvider userType, BigDecimal valueDiscount, VoucherType voucherType, BigDecimal valueNeed, Long limitPerUser, BigDecimal maxDiscount, Long quantity, VoucherStatus status, String image, String description, Long countUserVoucher) {
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
        this.countUserVoucher = countUserVoucher;
    }
}