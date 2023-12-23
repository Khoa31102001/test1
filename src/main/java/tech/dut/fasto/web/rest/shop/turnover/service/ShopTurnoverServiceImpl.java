package tech.dut.fasto.web.rest.shop.turnover.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.dut.fasto.common.domain.Shop;
import tech.dut.fasto.common.domain.enumeration.BillStatus;
import tech.dut.fasto.common.domain.enumeration.UserStatus;
import tech.dut.fasto.common.domain.enumeration.VoucherProvider;
import tech.dut.fasto.common.repository.BillItemRepository;
import tech.dut.fasto.common.repository.BillRepository;
import tech.dut.fasto.common.repository.ShopRepository;
import tech.dut.fasto.common.service.impl.MessageService;
import tech.dut.fasto.config.security.SecurityUtils;
import tech.dut.fasto.errors.FastoAlertException;
import tech.dut.fasto.web.rest.shop.turnover.dto.response.TopProductResponseDto;
import tech.dut.fasto.web.rest.shop.turnover.dto.response.TurnoverProductResponseDto;
import tech.dut.fasto.web.rest.shop.turnover.dto.response.TurnoverResponseDto;
import tech.dut.fasto.web.rest.shop.turnover.dto.response.TurnoverTotalResponseDto;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShopTurnoverServiceImpl implements ShopTurnoverService {

    private final BillRepository billRepository;

    private final BillItemRepository billItemRepository;

    private final ShopRepository shopRepository;

    private final MessageService messageService;

    @Override
    @Transactional(readOnly = true)
    public TurnoverResponseDto getRevenue(long dateFrom, long dateTo) {
        
        String email = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.revenue.get.failed"), messageService.getMessage("error.authenticate.unauthorized.user")));
        Shop shop = shopRepository.findByUserEmailAndUserStatus(email, UserStatus.ACTIVATED).orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.revenue.get.failed"), messageService.getMessage("error.shop.is.inactive")));

        Instant localDateFrom = Instant.ofEpochSecond(dateFrom).truncatedTo(ChronoUnit.DAYS);

        Instant localDateTo = Instant.ofEpochSecond(dateTo).plus(1,ChronoUnit.DAYS).truncatedTo(ChronoUnit.DAYS);
        TurnoverTotalResponseDto turnoverTotalResponseDto = billRepository.getTotalTurnover(localDateFrom, localDateTo, shop.getId(), BillStatus.DONE);
        if(null ==turnoverTotalResponseDto)
        {
            turnoverTotalResponseDto = new TurnoverTotalResponseDto();
        }

        List<TurnoverProductResponseDto> listProducts = billItemRepository.getAllProductPurchase(localDateFrom, localDateTo, shop.getId());
        turnoverTotalResponseDto.setTotalPriceDiscountOfAdmin(billRepository.getTotalVoucher(VoucherProvider.ADMIN, localDateFrom, localDateTo, shop.getId(), BillStatus.DONE));
        if (null == turnoverTotalResponseDto.getTotalPriceDiscountOfAdmin()){
            turnoverTotalResponseDto.setTotalPriceDiscountOfAdmin(BigDecimal.valueOf(0l));

        }
        if (null == turnoverTotalResponseDto.getTotalPriceDiscountOfAdmin()){
            turnoverTotalResponseDto.setTotalPriceDiscountOfAdmin(BigDecimal.valueOf(0l));

        }
        if (null == turnoverTotalResponseDto.getTotalPriceDiscount()){
            turnoverTotalResponseDto.setTotalPriceDiscount(BigDecimal.valueOf(0l));
        }
        if (null == turnoverTotalResponseDto.getTotalPrice()){
            turnoverTotalResponseDto.setTotalPrice(BigDecimal.valueOf(0l));
        }
        if (null == turnoverTotalResponseDto.getTotalPriceProductOrigin()){
            turnoverTotalResponseDto.setTotalPriceProductOrigin(BigDecimal.valueOf(0l));
        }
        turnoverTotalResponseDto.setTotalPriceDiscountOfShop(turnoverTotalResponseDto.getTotalPriceDiscount().subtract(turnoverTotalResponseDto.getTotalPriceDiscountOfAdmin()));
        turnoverTotalResponseDto.setTotalRealPriceOfAdmin(turnoverTotalResponseDto.getTotalPrice().multiply(BigDecimal.valueOf(5l)).divide(BigDecimal.valueOf(100)).subtract(turnoverTotalResponseDto.getTotalPriceDiscountOfAdmin()));
        turnoverTotalResponseDto.setTotalRealPriceOfShop(turnoverTotalResponseDto.getTotalPrice().subtract(turnoverTotalResponseDto.getTotalRealPriceOfAdmin()));

        TurnoverResponseDto turnoverResponseDto = new TurnoverResponseDto();

        turnoverResponseDto.setTurnoverTotalResponseDto(turnoverTotalResponseDto);
        turnoverResponseDto.setProductResponseDto(listProducts);

        return turnoverResponseDto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TopProductResponseDto> getTopProduct(Pageable pageable, long dateFrom, long dateTo) {
        String email = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.revenue.top.product.failed"), messageService.getMessage("error.authenticate.unauthorized.user")));
        Shop shop = shopRepository.findByUserEmailAndUserStatus(email, UserStatus.ACTIVATED).orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.revenue.top.product.failed"), messageService.getMessage("error.shop.is.inactive")));
        Instant localDateFrom = Instant.ofEpochSecond(dateFrom).truncatedTo(ChronoUnit.DAYS);
        Instant localDateTo = Instant.ofEpochSecond(dateTo).plus(1,ChronoUnit.DAYS).truncatedTo(ChronoUnit.DAYS);
        List<TopProductResponseDto> responseDtoList = billItemRepository.getTopProduct(localDateFrom, localDateTo, shop.getId(), pageable, BillStatus.DONE).getContent();
        responseDtoList = responseDtoList.stream().sorted((s1, s2) -> s2.getQuantity().compareTo(s1.getQuantity())).collect(Collectors.toList());
        return responseDtoList;
    }
}
