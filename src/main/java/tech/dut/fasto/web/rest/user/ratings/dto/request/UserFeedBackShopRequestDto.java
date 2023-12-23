package tech.dut.fasto.web.rest.user.ratings.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
public class UserFeedBackShopRequestDto implements Serializable {
    @Serial
    private static final long serialVersionUID = -7407897989173092262L;
    private Long shopId;
    private Long billId;
    private Double start;
    private String content;
}
