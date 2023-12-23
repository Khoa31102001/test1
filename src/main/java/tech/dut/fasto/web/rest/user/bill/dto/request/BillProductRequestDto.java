package tech.dut.fasto.web.rest.user.bill.dto.request;

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
public class BillProductRequestDto implements Serializable {


    @Serial
    private static final long serialVersionUID = 2504535965220785013L;
    private Long shopId;
    private Long voucherId;
    private List<BillProductDetailRequestDto> productDetailRequestDtos;
    private String deviceToken;
    private Double x;
    private Double y;
}
