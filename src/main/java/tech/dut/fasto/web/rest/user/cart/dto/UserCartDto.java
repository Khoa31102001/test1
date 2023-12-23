package tech.dut.fasto.web.rest.user.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tech.dut.fasto.common.dto.CartDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserCartDto implements Serializable {


    @Serial
    private static final long serialVersionUID = -2733091788299403237L;
    @NotNull(message = "validate.not.empty")
    private Long shopId;

    @NotBlank(message = "validate.not.empty")
    private String deviceToken;

    private List<CartDto> cartDtos;
}
