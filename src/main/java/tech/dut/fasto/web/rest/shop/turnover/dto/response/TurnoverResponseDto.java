package tech.dut.fasto.web.rest.shop.turnover.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TurnoverResponseDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 7676483849378020158L;

    TurnoverTotalResponseDto turnoverTotalResponseDto;
    List<TurnoverProductResponseDto> productResponseDto;
}

