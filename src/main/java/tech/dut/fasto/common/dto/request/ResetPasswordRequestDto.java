package tech.dut.fasto.common.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
public class ResetPasswordRequestDto implements Serializable {

    @Serial
    private static final long serialVersionUID = -1071862705703843642L;

    @NotBlank(message = "new password is required")
    private String newPassword;

    @NotBlank(message = "confirm new password is required")
    private String confirmNewPassword;

}
