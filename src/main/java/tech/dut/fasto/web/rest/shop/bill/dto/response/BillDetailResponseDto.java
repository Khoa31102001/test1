package tech.dut.fasto.web.rest.shop.bill.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tech.dut.fasto.common.domain.enumeration.BillStatus;
import tech.dut.fasto.common.domain.enumeration.PaymentType;
import tech.dut.fasto.common.domain.enumeration.VoucherProvider;
import tech.dut.fasto.common.domain.enumeration.VoucherType;
import tech.dut.fasto.config.jackson.InstantSerializerCustomizer;
import tech.dut.fasto.web.rest.shop.billitem.dto.response.BillItemResponseDto;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BillDetailResponseDto implements Serializable {


    @Serial
    private static final long serialVersionUID = 1320007416031092111L;
    private Long billId;
    private List<BillItemResponseDto> billItemResponseDtos;
    private BigDecimal totalOrigin;
    private BigDecimal totalPayment;
    private BigDecimal totalDiscount;
    private PaymentType paymentType;
    private String code;

    @JsonSerialize(using = InstantSerializerCustomizer.class)
    private Instant expiredDate;
    private BillStatus status;

    private Long userId;
    private String userFirstName;
    private String userLastName;

    @JsonSerialize(using = InstantSerializerCustomizer.class)
    private Instant createdAt;

    private Long shopId;

    private String shopName;

    private String logo;

    private Long addressId;

    private String city;


    private String district;

    private String town;


    private String street;


    private Double x;

    private Double y;


    private Long voucherId;

    private String voucherName;
    
    private BigDecimal shippingFee;

    @JsonSerialize(using = InstantSerializerCustomizer.class)
    private Instant voucherStartAt;

    @JsonSerialize(using = InstantSerializerCustomizer.class)
    private Instant voucherEndedAt;

    private VoucherProvider userType;

    private BigDecimal valueDiscount;

    private VoucherType voucherType;

    private BigDecimal valueNeed;

    private Boolean isRating;

    private Double rating;

    private String voucherDiscountImage;

    private String userImage;

}
