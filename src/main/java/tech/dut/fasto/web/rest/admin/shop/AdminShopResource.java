package tech.dut.fasto.web.rest.admin.shop;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tech.dut.fasto.common.anotation.SwaggerPageable;
import tech.dut.fasto.common.domain.enumeration.UserStatus;
import tech.dut.fasto.config.security.AuthoritiesConstants;
import tech.dut.fasto.web.rest.admin.shop.dto.AdminShopResponseDto;
import tech.dut.fasto.web.rest.admin.shop.service.AdminShopService;

@RestController
@RequestMapping("/admin/management/shops")
@RequiredArgsConstructor
@Validated
@PreAuthorize("hasAuthority('" + AuthoritiesConstants.ADMIN + "')")
public class AdminShopResource {

    private final AdminShopService adminShopService;

    @ApiOperation("API for active Shop")
    @PatchMapping(path = "/active/{id}")
    public ResponseEntity<String> activeShop(@PathVariable("id") Long id) {
        adminShopService.activeShop(id);
        return ResponseEntity.ok().build();
    }

    @ApiOperation("API for block Shop")
    @PatchMapping(path = "/shops/block/{id}")
    public ResponseEntity<String> blockShop(@PathVariable("id") Long id) {
        adminShopService.blockShop(id);
        return ResponseEntity.ok().build();
    }

    @ApiOperation("API for get detail shop")
    @GetMapping(path = "/shops/{id}")
    public ResponseEntity<AdminShopResponseDto> getDetailShops(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(adminShopService.getDetailShop(id));
    }

    @ApiOperation("API for get all users")
    @GetMapping
    @SwaggerPageable
    public ResponseEntity<Page<AdminShopResponseDto>> getAllUser(
            @PageableDefault(sort = "modifiedAt", direction = Sort.Direction.DESC)
            Pageable pageable,
            @ApiParam(value = "Search by name")
            @RequestParam(required = false) String query,
            @ApiParam(value = "Search by status")
            @RequestParam(required = false) UserStatus status) {
        return ResponseEntity.ok().body(adminShopService.getAllShops(pageable, status, query));
    }


}
