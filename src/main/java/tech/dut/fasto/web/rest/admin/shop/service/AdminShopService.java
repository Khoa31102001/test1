package tech.dut.fasto.web.rest.admin.shop.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tech.dut.fasto.common.domain.enumeration.UserStatus;
import tech.dut.fasto.web.rest.admin.shop.dto.AdminShopResponseDto;

public interface AdminShopService {
    void blockShop(Long shopId);

    void activeShop(Long shopId);

    Page<AdminShopResponseDto> getAllShops(Pageable pageable, UserStatus status, String query);

    AdminShopResponseDto getDetailShop(Long id);
}
