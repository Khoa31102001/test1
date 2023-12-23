package tech.dut.fasto.web.rest.user.vnpay.dto.response;

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
public class UserCardResponseDto implements Serializable {
    @Serial
    private static final long serialVersionUID = -8708578607387098344L;
    private Long id;
    private String cardNumber;
    private String bank;
}
