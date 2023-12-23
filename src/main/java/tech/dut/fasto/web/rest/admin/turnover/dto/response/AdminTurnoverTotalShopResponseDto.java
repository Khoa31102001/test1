package tech.dut.fasto.web.rest.admin.turnover.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminTurnoverTotalShopResponseDto implements Serializable {

    @Serial
    private static final long serialVersionUID = -7125884050121913750L;

    private BigDecimal totalPriceProductOrigin;
    private BigDecimal totalPriceDiscount;
    private BigDecimal totalPriceDiscountOfShop;
    private BigDecimal totalPriceDiscountOfAdmin;
    private BigDecimal totalRealPrice;
    private BigDecimal totalRealPriceOfShop;
    private BigDecimal totalRealPriceOfAdmin;
    private String shopName;
    private Long shopId;
    private String logo;

    private String description;
    private Double ratings;

    private Double startRatings;

    public AdminTurnoverTotalShopResponseDto(BigDecimal totalPriceProductOrigin, BigDecimal totalPriceDiscount, BigDecimal totalRealPrice, String shopName, Long shopId, String logo, String description, Double ratings, Double startRatings) {
        this.totalPriceProductOrigin = totalPriceProductOrigin;
        this.totalPriceDiscount = totalPriceDiscount;
        this.totalRealPrice = totalRealPrice;
        this.shopName = shopName;
        this.shopId = shopId;
        this.logo = logo;
        this.description = description;
        this.ratings = ratings;
        this.startRatings = startRatings;
    }
}
