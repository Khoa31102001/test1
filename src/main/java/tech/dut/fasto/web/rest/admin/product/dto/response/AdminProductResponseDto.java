package tech.dut.fasto.web.rest.admin.product.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import tech.dut.fasto.common.domain.Product;
import tech.dut.fasto.common.domain.enumeration.ProductStatus;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A DTO for the {@link Product} entity
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdminProductResponseDto implements Serializable {
    @Serial
    private static final long serialVersionUID = -2268324331633470147L;

    private Long id;

    private String name;

    private String image;

    private String description;

    private BigDecimal price;

    private ProductStatus status;

    private Boolean deleteFlag;

    private Long countPay;

    private Long shopId;

    private String shopName;

    private Long categoryId;
    private String categoryName;

    public AdminProductResponseDto(Long id, String name, String image, String description, BigDecimal price, ProductStatus status, Boolean deleteFlag, Long countPay, Long shopId, String shopName, Long categoryId, String categoryName) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.description = description;
        this.price = price;
        this.status = status;
        this.deleteFlag = deleteFlag;
        this.countPay = countPay;
        this.shopId = shopId;
        this.shopName = shopName;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

}