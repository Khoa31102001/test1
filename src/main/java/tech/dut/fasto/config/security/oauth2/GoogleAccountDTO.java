package tech.dut.fasto.config.security.oauth2;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GoogleAccountDTO {
    private String id;
    private String name;
    private String given_name;
    private String family_name;
    private String picture;
    private String email;
}
