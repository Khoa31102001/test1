package tech.dut.fasto.web.rest.shop.authentication.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
public class DepositShopDto implements Serializable {
    @Serial
    private static final long serialVersionUID = -3112079492382662124L;
    private Boolean isDeposit;
}
