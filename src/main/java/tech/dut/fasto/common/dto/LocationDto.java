package tech.dut.fasto.common.dto;

import lombok.Getter;
import lombok.Setter;
import tech.dut.fasto.common.domain.Location;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;

/**
 * A DTO for the {@link Location} entity
 */
@Getter
@Setter
public class LocationDto implements Serializable {

    @Serial
    private static final long serialVersionUID = -7219930384768601203L;
    @NotBlank(message = "City is required")
    private  String city;

    @NotBlank(message = "State is required")
    private  String stateProvince;
    @NotBlank(message = "Country is required")
    private  String country;

    @NotBlank(message = "address is required")
    private  String streetAddress;
    @NotNull(message = "Coordinates x is required")
    private  Double x;
    @NotNull(message = "Coordinates y is required")
    private  Double y;
}