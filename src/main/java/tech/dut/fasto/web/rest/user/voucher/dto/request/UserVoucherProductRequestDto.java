package tech.dut.fasto.web.rest.user.voucher.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class UserVoucherProductRequestDto implements Serializable {
    @Serial
    private static final long serialVersionUID = -7986888209569056635L;
    @NotNull
    private Long shopId;
    @NotNull
    private List<UserVoucherProductDetailRequestDto> orderDetails;
}

