package tech.dut.fasto.web.rest.user.voucher.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
public class UserVoucherProductDetailRequestDto implements Serializable {
    @Serial
    private static final long serialVersionUID = -8114527394929305753L;
    @NotNull
    private Long amount;
    @NotNull
    private Long productId;
}
