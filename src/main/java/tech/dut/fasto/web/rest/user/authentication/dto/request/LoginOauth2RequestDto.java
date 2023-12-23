package tech.dut.fasto.web.rest.user.authentication.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
public class LoginOauth2RequestDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 4304573683211641303L;
    @NotBlank(message = "validate.not.empty")
    private String accessToken;
    @NotBlank(message = "validate.not.empty")
    private String tokenType;

    private String deviceToken;
}
