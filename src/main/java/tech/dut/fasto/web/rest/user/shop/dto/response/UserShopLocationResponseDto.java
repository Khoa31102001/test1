package tech.dut.fasto.web.rest.user.shop.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
@Setter
@NoArgsConstructor
public class UserShopLocationResponseDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 6374779965165229419L;
    private Long id;
    private String name;
    private String description;
    private String logo;
    private Double x;
    private Double y;
    private String street;
    private Object distance;
    private Double ratings;

    private Double startRatings;

    public UserShopLocationResponseDto(Long id, String name, String description, String logo, Double x, Double y, String street, Double ratings) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.logo = logo;
        this.x = x;
        this.y = y;
        this.street = street;
        this.ratings = ratings;
    }

    public UserShopLocationResponseDto(Long id, String name, String description, String logo, Double x, Double y, String street, Double ratings, Double startRatings) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.logo = logo;
        this.x = x;
        this.y = y;
        this.street = street;
        this.ratings = ratings;
        this.startRatings = startRatings;
    }

    public UserShopLocationResponseDto(Long id, String name, String description, String logo, Double x, Double y, String street, Double ratings, Double startRatings, Double distance) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.logo = logo;
        this.x = x;
        this.y = y;
        this.street = street;
        this.ratings = ratings;
        this.startRatings = BigDecimal.valueOf( startRatings).setScale(0, RoundingMode.HALF_UP).doubleValue();
        this.distance = distance;
    }
}
