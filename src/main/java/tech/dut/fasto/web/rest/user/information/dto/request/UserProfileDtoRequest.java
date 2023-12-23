package tech.dut.fasto.web.rest.user.information.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tech.dut.fasto.common.domain.enumeration.Gender;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDtoRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1953444085171558417L;
    private String firstName;
    private String lastName;
    private Gender gender;
    private Long birthday;
    private String userImage;
}
