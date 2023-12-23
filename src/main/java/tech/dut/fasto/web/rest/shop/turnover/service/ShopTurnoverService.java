package tech.dut.fasto.web.rest.shop.turnover.service;

import org.springframework.data.domain.Pageable;
import tech.dut.fasto.web.rest.shop.turnover.dto.response.TopProductResponseDto;
import tech.dut.fasto.web.rest.shop.turnover.dto.response.TurnoverResponseDto;

import java.util.List;

public interface ShopTurnoverService {

    TurnoverResponseDto getRevenue(long dateFrom, long dateTo);

    List<TopProductResponseDto> getTopProduct(Pageable pageable, long dateFrom, long dateTo);
}
