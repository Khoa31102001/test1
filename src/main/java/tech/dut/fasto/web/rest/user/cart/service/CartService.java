package tech.dut.fasto.web.rest.user.cart.service;

import tech.dut.fasto.common.dto.CartDto;
import tech.dut.fasto.web.rest.user.cart.dto.UserCartDto;

import java.util.List;
import java.util.Map;

public interface CartService {

    Map<String,List<CartDto>> getAllCarts(String deviceToken);

    void updateCart(UserCartDto cartDto);

    void removeProductCart(List<Long> productIds, Long shopId, String deviceToken);

}
