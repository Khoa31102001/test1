package tech.dut.fasto.web.rest.user.information.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tech.dut.fasto.common.domain.enumeration.Gender;
import tech.dut.fasto.config.jackson.InstantDeserializerCustomizer;
import tech.dut.fasto.config.jackson.InstantSerializerCustomizer;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDtoResponse implements Serializable {


    @Serial
    private static final long serialVersionUID = -2597219293990129411L;
    private Long userId;
    private Long userInformationId;
    private String firstName;
    private String lastName;
    private Gender gender;
    @JsonDeserialize(using = InstantDeserializerCustomizer.class)
    @JsonSerialize(using = InstantSerializerCustomizer.class)
    private Instant birthday;
    private String userImage;
    
    
}
