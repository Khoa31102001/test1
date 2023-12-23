package tech.dut.fasto.common.dto;

import lombok.Getter;
import lombok.Setter;
import tech.dut.fasto.util.constants.Constants;

@Getter
@Setter
public class NotificationDto {
    private String title = Constants.TITLE;
    private String body;
}
