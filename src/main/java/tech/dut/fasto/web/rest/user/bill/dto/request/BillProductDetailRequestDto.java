package tech.dut.fasto.web.rest.user.bill.dto.request;

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
public class BillProductDetailRequestDto implements Serializable {
    @Serial
    private static final long serialVersionUID = -2674233678292355771L;
    private Long amount;
    private Long productId;
}
