package tech.dut.fasto.common.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
public class LogoutRequestDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 8368963067812266679L;
    private String deviceToken;
}
