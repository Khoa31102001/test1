package tech.dut.fasto.web.rest.shop.image;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tech.dut.fasto.config.security.AuthoritiesConstants;
import tech.dut.fasto.web.rest.shop.image.dto.request.ShopImageRequestDto;
import tech.dut.fasto.web.rest.shop.image.service.ShopImageService;

import java.util.List;

@RestController
@RequestMapping("/shop/management")
@RequiredArgsConstructor
@Validated
@PreAuthorize("hasAuthority('" + AuthoritiesConstants.ADMIN + "')")
public class ShopImageResource {
    private final ShopImageService shopImageService;

    @PostMapping("/voucher/{id}/images")
    public ResponseEntity<?> createVoucherImage(@PathVariable Long id, @RequestBody ShopImageRequestDto shopImageRequestDto){
        shopImageService.createImageForVoucher(shopImageRequestDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/shop/{id}/images")
    public ResponseEntity<?> createShopImage(@PathVariable Long id, @RequestBody ShopImageRequestDto shopImageRequestDto){
        shopImageService.createImageForShop(shopImageRequestDto);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/product/{id}/images")
    public ResponseEntity<?> createProductImage(@PathVariable Long id, @RequestBody ShopImageRequestDto shopImageRequestDto){
        shopImageService.createImageForProduct(shopImageRequestDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/voucher/{id}/images")
    public ResponseEntity<List<String>> getVoucherImage(@PathVariable Long id){

        return ResponseEntity.ok(shopImageService.getImageForVoucher(id));
    }

    @GetMapping("/shop/{id}/images")
    public ResponseEntity<List<String>> getShopImage(@PathVariable Long id){

        return ResponseEntity.ok(shopImageService.getImageForShop(id));
    }

    @GetMapping("/product/{id}/images")
    public ResponseEntity<List<String>> getProductImage(@PathVariable Long id){

        return ResponseEntity.ok(shopImageService.getImageForProduct(id));
    }

}
