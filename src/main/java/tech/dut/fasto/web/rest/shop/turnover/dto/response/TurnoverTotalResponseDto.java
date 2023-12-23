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
public class TurnoverTotalResponseDto implements Serializable {


    @Serial
    private static final long serialVersionUID = 6078693775999957703L;
    BigDecimal totalPriceProductOrigin;
    BigDecimal totalPriceDiscount;
    BigDecimal totalPrice;
    BigDecimal totalPriceDiscountOfShop;
    BigDecimal totalPriceDiscountOfAdmin;
    BigDecimal totalRealPriceOfShop;
    BigDecimal totalRealPriceOfAdmin;

    public TurnoverTotalResponseDto(BigDecimal totalPriceProductOrigin, BigDecimal totalPriceDiscount, BigDecimal totalPrice) {
        this.totalPriceProductOrigin = totalPriceProductOrigin;
        this.totalPriceDiscount = totalPriceDiscount;
        this.totalPrice = totalPrice;
        this.totalPriceDiscountOfShop =  BigDecimal.valueOf(0l);
        this.totalPriceDiscountOfAdmin=  BigDecimal.valueOf(0l);
        this.totalRealPriceOfShop=  BigDecimal.valueOf(0l);
        this.totalRealPriceOfAdmin=  BigDecimal.valueOf(0l);
    }
}
