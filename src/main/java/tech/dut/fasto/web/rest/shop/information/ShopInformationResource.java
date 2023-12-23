package tech.dut.fasto.web.rest.shop.information;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tech.dut.fasto.config.security.AuthoritiesConstants;
import tech.dut.fasto.web.rest.shop.information.dto.request.ShopInformationRequestDto;
import tech.dut.fasto.web.rest.shop.information.dto.response.ShopProfileResponseDto;
import tech.dut.fasto.web.rest.shop.information.service.ShopInformationService;

@RestController
@RequestMapping("/shop/information")
@RequiredArgsConstructor
@Validated
@PreAuthorize("hasAuthority('" + AuthoritiesConstants.SHOP + "')")
public class ShopInformationResource {
    private final ShopInformationService shopInformationService;

    @GetMapping
    public ResponseEntity<ShopProfileResponseDto> getShopInfo(){
        return ResponseEntity.ok(shopInformationService.getShopInfo());
    }

    @PatchMapping
    public ResponseEntity<?> updateInfo(@RequestBody ShopInformationRequestDto shopInformationRequestDto){
        shopInformationService.updateShopInfo(shopInformationRequestDto);
        return ResponseEntity.ok().build();
    }
}
