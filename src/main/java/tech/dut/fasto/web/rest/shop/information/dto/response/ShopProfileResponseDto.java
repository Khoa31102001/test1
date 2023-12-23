package tech.dut.fasto.web.rest.shop.information.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShopProfileResponseDto implements Serializable {
    @Serial
    private static final long serialVersionUID = -5259574824614538691L;
    private Long id;
    private String name;
    private String logo;
    private String description;
    private String phone;
    private Double startRatings;
    private Double rantings;
    private List<String> imageShops;
    private String city;
    private String country;
    private String province;
    private String street;
    private Double x;
    private Double y;
}
