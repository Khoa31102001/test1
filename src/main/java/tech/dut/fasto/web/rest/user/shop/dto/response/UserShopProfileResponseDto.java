package tech.dut.fasto.web.rest.user.shop.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserShopProfileResponseDto implements Serializable {


    @Serial
    private static final long serialVersionUID = 1716776747399148004L;
    private Long id;
    private String name;
    private String banner;
    private String description;
    private String phone;
    private Double startRatings;
    private Double ranting;
    private String city;
    private String stateProvince;
    private String country;
    private String streetAddress;
    private Double x;
    private Double y;
    private Boolean isFavorite;
    private Double distance;

    public UserShopProfileResponseDto(Long id, String name, String banner, String description, String phone, Double startRatings, Double ranting, String city, String stateProvince, String country, String streetAddress, String postalCode, Double x, Double y) {
        this.id = id;
        this.name = name;
        this.banner = banner;
        this.description = description;
        this.phone = phone;
        this.startRatings = startRatings;
        this.ranting = ranting;
        this.city = city;
        this.stateProvince = stateProvince;
        this.country = country;
        this.streetAddress = streetAddress;
        this.x = x;
        this.y = y;
    }
}
