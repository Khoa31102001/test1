package tech.dut.fasto.web.rest.admin.shop.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import tech.dut.fasto.common.anotation.Default;
import tech.dut.fasto.common.domain.Shop;
import tech.dut.fasto.common.domain.enumeration.UserStatus;
import tech.dut.fasto.common.dto.LocationDto;

import java.io.Serial;
import java.io.Serializable;

/**
 * A DTO for the {@link Shop} entity
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdminShopResponseDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 8630704871613992350L;
    private Long id;
    private String name;
    private String banner;
    private String description;
    private UserStatus status;
    private String phone;
    private Double startRatings;
    private Double ratings;
    private LocationDto address;
    private Boolean isDeposit;


    public AdminShopResponseDto(Long id, String name, UserStatus status, String description, String banner, String phone, Double ratings, Double startRatings, Boolean isDeposit) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.description = description;
        this.phone = phone;
        this.banner = banner;
        this.ratings = ratings;
        this.startRatings = startRatings;
        this.isDeposit = isDeposit;
    }

    @Default
    public AdminShopResponseDto(Long id, String name, String banner, String description, UserStatus status, String phone, Double startRatings, Double ratings, LocationDto address) {
        this.id = id;
        this.name = name;
        this.banner = banner;
        this.description = description;
        this.status = status;
        this.phone = phone;
        this.startRatings = startRatings;
        this.ratings = ratings;
        this.address = address;
    }
}