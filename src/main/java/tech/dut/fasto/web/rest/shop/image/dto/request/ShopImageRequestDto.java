package tech.dut.fasto.web.rest.shop.image.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class ShopImageRequestDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 951259852328753894L;
    @NotNull
    private Long id;
    @NotNull
    private List<String> images;
}
