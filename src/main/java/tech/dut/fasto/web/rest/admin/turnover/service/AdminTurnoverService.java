package tech.dut.fasto.web.rest.admin.turnover.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tech.dut.fasto.web.rest.admin.turnover.dto.response.AdminTurnoverTotalShopResponseDto;

public interface AdminTurnoverService {
    Page<AdminTurnoverTotalShopResponseDto> getRevenue(Long dateFrom, Long dateTo, Pageable pageable);
    AdminTurnoverTotalShopResponseDto getRevenueShop(Long dateFrom, Long dateTo, Long shopId);
    AdminTurnoverTotalShopResponseDto getAllRevenue(Long dateFrom, Long dateTo);
}
