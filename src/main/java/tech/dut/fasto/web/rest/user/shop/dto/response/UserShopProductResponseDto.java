package tech.dut.fasto.web.rest.user.shop.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tech.dut.fasto.common.domain.enumeration.ProductStatus;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserShopProductResponseDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 6677904732374528750L;
    private  Long id;

    private  String name;

    private  String image;

    private  String description;

    private BigDecimal price;

    private ProductStatus status;

    private  String categoryName;

    private Long categoryId;

    private Long countPay;

    private Long shopId;

    private String shopName;
}
