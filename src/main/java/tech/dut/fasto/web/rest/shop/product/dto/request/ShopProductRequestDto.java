package tech.dut.fasto.web.rest.shop.product.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import tech.dut.fasto.common.domain.Product;
import tech.dut.fasto.common.domain.enumeration.ProductStatus;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A DTO for the {@link Product} entity
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShopProductRequestDto implements Serializable {
    @Serial
    private static final long serialVersionUID = -2268324331633470147L;

    private  Long id;

    @NotBlank(message = "validate.not.empty")
    private  String name;

    @NotBlank(message = "validate.not.empty")
    private  String image;

    private  String description;

    @NotNull(message = "validate.not.empty")
    private  BigDecimal price;

    @NotNull(message = "validate.not.empty")
    private ProductStatus status;

    private Boolean deleteFlag;

    private Long countPay;

    @NotNull(message = "validate.not.empty")
    private Long categoryId;
}