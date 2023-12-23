package tech.dut.fasto.web.rest.shop.information.dto.request;

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
public class ShopInformationRequestDto implements Serializable {
    @Serial
    private static final long serialVersionUID = -7290594916611700936L;
    private String name;

    private String banner;

    private List<String> images;

    private String city;

    private String country;
    private String street;
    private String province;
    private Double x;
    private Double y;
}
