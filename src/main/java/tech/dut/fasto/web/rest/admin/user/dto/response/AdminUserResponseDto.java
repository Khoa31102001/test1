package tech.dut.fasto.web.rest.admin.user.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import tech.dut.fasto.common.anotation.Default;
import tech.dut.fasto.common.domain.User;
import tech.dut.fasto.common.domain.enumeration.Provider;
import tech.dut.fasto.common.domain.enumeration.UserStatus;
import tech.dut.fasto.common.dto.UserInformationDto;

import java.io.Serial;
import java.io.Serializable;

/**
 * A DTO for the {@link User} entity
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdminUserResponseDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 6588772991055951513L;
    private Long id;
    private String email;
    private Provider provider;
    private String phoneNumber;
    private UserStatus status;
    private UserInformationDto userInformation;

    public AdminUserResponseDto(Long id, String email, Provider provider, String phoneNumber, UserStatus status) {
        this.id = id;
        this.email = email;
        this.provider = provider;
        this.phoneNumber = phoneNumber;
        this.status = status;
    }

    @Default
    public AdminUserResponseDto(Long id, String email, Provider provider, String phoneNumber, UserStatus status, UserInformationDto userInformation) {
        this.id = id;
        this.email = email;
        this.provider = provider;
        this.phoneNumber = phoneNumber;
        this.status = status;
        this.userInformation = userInformation;
    }
}