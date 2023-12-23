package tech.dut.fasto.common.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.net.URL;

@Getter
@Setter
public class PreSignedURL {
    @ApiModelProperty(readOnly = true)
    private URL preSignedURL;

    @ApiModelProperty(readOnly = true)
    private String url;
}
