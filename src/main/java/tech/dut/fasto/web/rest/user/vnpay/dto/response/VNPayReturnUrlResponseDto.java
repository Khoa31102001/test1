package tech.dut.fasto.web.rest.user.vnpay.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VNPayReturnUrlResponseDto implements Serializable {
    @Serial
    private static final long serialVersionUID = -1840179332938659893L;
    private String responseCode;
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String signData;
}
