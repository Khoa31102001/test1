package tech.dut.fasto.web.rest.shop.bill.dto.response;

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
public class BillCodeResponseDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 3247153680825443282L;
    private String code;
}
