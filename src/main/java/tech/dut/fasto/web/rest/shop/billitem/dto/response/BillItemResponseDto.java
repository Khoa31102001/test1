package tech.dut.fasto.web.rest.shop.billitem.dto.response;

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
public class BillItemResponseDto implements Serializable {
    @Serial
    private static final long serialVersionUID = -7215783375873268199L;
    private String nameProduct;
    private Long amount;
    private BigDecimal price;
    private String nameShop;
    private String nameCategory;
    private String image;
}
