package tech.dut.fasto.web.rest.shop.product.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import tech.dut.fasto.common.anotation.Default;
import tech.dut.fasto.common.domain.Product;
import tech.dut.fasto.common.domain.enumeration.ProductStatus;
import tech.dut.fasto.common.dto.CategoryDto;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A DTO for the {@link Product} entity
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShopProductResponseDto implements Serializable {


    @Serial
    private static final long serialVersionUID = -8398217234402724942L;
    private Long id;
    
    private String name;
    
    private String image;

    private String description;
    
    private BigDecimal price;
    
    private ProductStatus status;

    private Boolean deleteFlag;

    private Long countPay;
    
    private CategoryDto category;

    public ShopProductResponseDto(Long id, String name, String image, String description, BigDecimal price, ProductStatus status, Boolean deleteFlag, Long countPay) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.description = description;
        this.price = price;
        this.status = status;
        this.deleteFlag = deleteFlag;
        this.countPay = countPay;
    }

    @Default
    public ShopProductResponseDto(Long id, String name, String image, String description, BigDecimal price, ProductStatus status, Boolean deleteFlag, Long countPay, CategoryDto category) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.description = description;
        this.price = price;
        this.status = status;
        this.deleteFlag = deleteFlag;
        this.countPay = countPay;
        this.category = category;
    }
}