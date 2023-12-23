package tech.dut.fasto.web.rest.shop.authentication.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import tech.dut.fasto.common.dto.LocationDto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
public class SignUpShopDto implements Serializable {


    @Serial
    private static final long serialVersionUID = -205950116281447550L;
    @NotBlank(message = "validate.not.empty")
    @ApiModelProperty(required = true)
    private String name;

    @ApiModelProperty(required = true)
    private String banner;

    @ApiModelProperty(required = true)
    private String description;

    @NotBlank(message = "validate.not.empty")
    @ApiModelProperty(required = true)
    private String password;

    @Email(message = "validate.error.format")
    @NotBlank(message = "validate.not.empty")
    @ApiModelProperty(required = true)
    private String email;

    @Size(min = 10, max = 10, message = "validate.error.size")
    private String phoneNumber;

    @ApiModelProperty(required = true)
    @NotNull(message = "validate.not.empty")
    private LocationDto locationDto;
}
