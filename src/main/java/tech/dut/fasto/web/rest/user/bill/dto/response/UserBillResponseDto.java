package tech.dut.fasto.web.rest.user.bill.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.dut.fasto.common.domain.enumeration.BillStatus;
import tech.dut.fasto.config.jackson.InstantSerializerCustomizer;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserBillResponseDto implements Serializable {

    @Serial
    private static final long serialVersionUID = -330059005603724551L;
    private Long id;
    private BigDecimal totalOrigin;
    private BigDecimal totalPayment;
    private BigDecimal totalDiscount;

    private Long userId;

    private String userFirstName;

    private String userLastName;

    @JsonSerialize(using = InstantSerializerCustomizer.class)
    private Instant createdAt;

    private BillStatus status;

    private Long shopId;

    private String name;

    private String logo;

    private Long addressId;

    private String city;


    private String district;

    private String town;


    private String street;


    private Double x;

    private Double y;

    private Boolean isRating;

    private Double ratings;

    private String userImage;



    public UserBillResponseDto(Long id, BigDecimal totalOrigin, BigDecimal totalPayment, BigDecimal totalDiscount, Long userId, String userFirstName, String userLastName, Instant createdAt, BillStatus status, Boolean isRating, Double rating, String userImage) {
        this.id = id;
        this.totalOrigin = totalOrigin;
        this.totalPayment = totalPayment;
        this.totalDiscount = totalDiscount;
        this.userId = userId;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.createdAt = createdAt;
        this.status = status;
        this.isRating = isRating;
        this.ratings = rating;
        this.userImage = userImage;
    }

    public UserBillResponseDto(Long id, BigDecimal totalOrigin, BigDecimal totalPayment, BigDecimal totalDiscount, Instant createdAt, BillStatus status, Long shopId, String name, String logo, Long addressId, String city, String district, String town, String street, Double x, Double y, Boolean isRating, Double ratings) {
        this.id = id;
        this.totalOrigin = totalOrigin;
        this.totalPayment = totalPayment;
        this.totalDiscount = totalDiscount;
        this.createdAt = createdAt;
        this.status = status;
        this.shopId = shopId;
        this.name = name;
        this.logo = logo;
        this.addressId = addressId;
        this.city = city;
        this.district = district;
        this.town = town;
        this.street = street;
        this.x = x;
        this.y = y;
        this.isRating = isRating;
        this.ratings = ratings;
    }
}
