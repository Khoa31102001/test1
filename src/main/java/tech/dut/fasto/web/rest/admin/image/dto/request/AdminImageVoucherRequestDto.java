package tech.dut.fasto.web.rest.admin.image.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class AdminImageVoucherRequestDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 951259852328753894L;
    @NotNull(message = "validate.not.empty")
    private Long id;
    @NotNull(message = "validate.not.empty")
    private List<String> images;
}
