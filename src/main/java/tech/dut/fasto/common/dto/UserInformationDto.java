package tech.dut.fasto.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import tech.dut.fasto.common.domain.UserInformation;
import tech.dut.fasto.common.domain.enumeration.Gender;
import tech.dut.fasto.config.jackson.InstantSerializerCustomizer;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

/**
 * A DTO for the {@link UserInformation} entity
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserInformationDto implements Serializable {


    @Serial
    private static final long serialVersionUID = 2045075237409573295L;
    private Long id;

    private String firstName;

    private String lastName;

    private Gender gender;

    @JsonSerialize(using = InstantSerializerCustomizer.class)
    private Instant birthday;

    private String userImage;
}