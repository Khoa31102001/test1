package tech.dut.fasto.common.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
public class CodeAuthenticateRequestDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 4349888135218940003L;

    @NotBlank(message = "Code is required")
    private String code;
}
