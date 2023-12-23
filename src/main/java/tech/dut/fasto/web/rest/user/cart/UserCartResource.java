package tech.dut.fasto.web.rest.user.cart;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tech.dut.fasto.common.dto.CartDto;
import tech.dut.fasto.web.rest.user.cart.dto.UserCartDto;
import tech.dut.fasto.web.rest.user.cart.service.CartService;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user/management")
@RequiredArgsConstructor
@Validated
public class UserCartResource {
    private final CartService cartService;

    @GetMapping(path = "/carts")
    public ResponseEntity<Map<String, List<CartDto>>> getCarts(@RequestParam(name = "device") @NotBlank(message = "validate.not.empty") String deviceToken) {
        return ResponseEntity.ok(cartService.getAllCarts(deviceToken));
    }

    @PostMapping(path = "/carts")
    public ResponseEntity<Map<String, List<CartDto>>> updateCart(@RequestBody @Valid UserCartDto cartDto) {
        cartService.updateCart(cartDto);
        return ResponseEntity.ok(cartService.getAllCarts(cartDto.getDeviceToken()));
    }
}
