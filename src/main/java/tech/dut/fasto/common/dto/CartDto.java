package tech.dut.fasto.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartDto implements Serializable {


    @Serial
    private static final long serialVersionUID = 2196054935845575877L;
    private Long id;

    private String name;

    private BigDecimal price;

    private String image;

    private Long shopId;

    private Long amount;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CartDto that)) return false;
        return id.equals(that.id) && name.equals(that.name) && price.equals(that.price) && image.equals(that.image) && shopId.equals(that.shopId) && amount.equals(that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, image, shopId, amount);
    }
}