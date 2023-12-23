package tech.dut.fasto.web.rest.user.favorite.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tech.dut.fasto.common.domain.enumeration.ShopSchedule;

import java.io.Serial;
import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ShopFavoriteResponseDto implements Serializable {
    @Serial
    private static final long serialVersionUID = -8586901314244220559L;
    private Long id;
    private String name;
    private String banner;
    private String description;
    private ShopSchedule schedule;
    private String phone;
}
