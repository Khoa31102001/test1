package tech.dut.fasto.config.security.oauth2;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GoogleAccountInfoDto {
    @JsonProperty("resourceName")
    private String resourceName;
    private GoogleGender[] genders;
    private GoogleBirthDay[] birthdays;

}
