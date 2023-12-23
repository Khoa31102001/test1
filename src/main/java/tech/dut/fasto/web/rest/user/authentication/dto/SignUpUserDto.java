package tech.dut.fasto.web.rest.user.authentication.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
public class SignUpUserDto implements Serializable {
    @Serial
    private static final long serialVersionUID = -7866694352967523843L;
    @NotBlank(message = "validate.not.empty")
    @ApiModelProperty(required = true)
    private String firstname;

    @NotBlank(message = "validate.not.empty")
    @ApiModelProperty(required = true)
    private String lastname;

    @NotBlank(message = "validate.not.empty")
    @ApiModelProperty(required = true)
    private String password;

    private Long birthDay;

    @Email(message = "validate.error.format")
    @NotBlank(message = "validate.not.empty")
    @ApiModelProperty(required = true)
    private String email;

    @Size(min = 10, max = 10, message = "validate.error.size")
    private String phoneNumber;

    private String deviceToken;
}
