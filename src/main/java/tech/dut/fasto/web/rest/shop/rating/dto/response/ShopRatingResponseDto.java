package tech.dut.fasto.web.rest.shop.rating.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShopRatingResponseDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 7099692522232883217L;

    Long userId;
    String userFirstName;
    String userLastName;
    String userImage;

    String content;
    Double ratings;

}
