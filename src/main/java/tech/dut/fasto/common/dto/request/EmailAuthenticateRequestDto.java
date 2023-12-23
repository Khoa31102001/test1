package tech.dut.fasto.common.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
public class EmailAuthenticateRequestDto implements Serializable {
    @Serial
    private static final long serialVersionUID = -2524261991825877841L;
    @Email(message = "Email format is not correct")
    @NotBlank(message = "Email is required")
    private String email;

}
