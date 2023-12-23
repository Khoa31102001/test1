package tech.dut.fasto.web.rest.admin.turnover.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class AdminTurnoverDiscountShopDto {
    private Long shopId;
    private BigDecimal discountAdmin;
}
