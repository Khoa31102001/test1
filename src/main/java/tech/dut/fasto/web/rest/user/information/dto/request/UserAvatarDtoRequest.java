package tech.dut.fasto.web.rest.user.information.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
public class UserAvatarDtoRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = -4221537062392858518L;
    private String imageUser;
}
