package tech.dut.fasto.web.rest.user.shop.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tech.dut.fasto.web.rest.shop.voucher.dto.response.ShopVoucherResponseDto;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserShopResponseDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 5246517497394689819L;
    private  UserShopProfileResponseDto shopProfile;

    private List<UserShopProductResponseDto> listProduct;

    private List<ShopVoucherResponseDto> listVoucher;
}
