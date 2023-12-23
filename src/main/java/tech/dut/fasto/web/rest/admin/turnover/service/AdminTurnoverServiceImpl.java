package tech.dut.fasto.web.rest.admin.turnover.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.dut.fasto.common.domain.enumeration.BillStatus;
import tech.dut.fasto.common.domain.enumeration.VoucherProvider;
import tech.dut.fasto.common.repository.BillRepository;
import tech.dut.fasto.web.rest.admin.turnover.dto.response.AdminTurnoverDiscountShopDto;
import tech.dut.fasto.web.rest.admin.turnover.dto.response.AdminTurnoverTotalShopResponseDto;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminTurnoverServiceImpl implements AdminTurnoverService {
    private final BillRepository billRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<AdminTurnoverTotalShopResponseDto> getRevenue(Long dateFrom, Long dateTo, Pageable pageable) {
        Instant localDateFrom = Instant.ofEpochSecond(dateFrom).truncatedTo(ChronoUnit.DAYS);

        Instant localDateTo = Instant.ofEpochSecond(dateTo).plus(1, ChronoUnit.DAYS).truncatedTo(ChronoUnit.DAYS);
        Page<AdminTurnoverTotalShopResponseDto> adminTurnoverTotalShopResponseDtos = billRepository.getAllTotalTurnoverShop(localDateFrom, localDateTo, pageable, BillStatus.DONE);
        Map<Long, BigDecimal> adminTurnoverDiscountShopDtos = billRepository.getAllTotalVoucherAdmin(VoucherProvider.ADMIN, localDateFrom, localDateTo, BillStatus.DONE).stream().collect(Collectors.toMap(AdminTurnoverDiscountShopDto::getShopId, AdminTurnoverDiscountShopDto::getDiscountAdmin));
        for (AdminTurnoverTotalShopResponseDto adminTurnoverTotalShopResponseDto : adminTurnoverTotalShopResponseDtos) {
            if (null == adminTurnoverTotalShopResponseDto) {
                adminTurnoverTotalShopResponseDto = new AdminTurnoverTotalShopResponseDto();
            }
            adminTurnoverTotalShopResponseDto.setTotalPriceDiscountOfAdmin(adminTurnoverDiscountShopDtos.get(adminTurnoverTotalShopResponseDto.getShopId()));
            if (null == adminTurnoverTotalShopResponseDto.getTotalPriceDiscountOfAdmin()) {
                adminTurnoverTotalShopResponseDto.setTotalPriceDiscountOfAdmin(BigDecimal.valueOf(0l));
            }
            adminTurnoverTotalShopResponseDto.setTotalPriceDiscountOfShop(adminTurnoverTotalShopResponseDto.getTotalPriceDiscount().subtract(adminTurnoverTotalShopResponseDto.getTotalPriceDiscountOfAdmin()));
            adminTurnoverTotalShopResponseDto.setTotalRealPriceOfAdmin(adminTurnoverTotalShopResponseDto.getTotalRealPrice().multiply(BigDecimal.valueOf(5l)).divide(BigDecimal.valueOf(100)).subtract(adminTurnoverTotalShopResponseDto.getTotalPriceDiscountOfAdmin()));
            adminTurnoverTotalShopResponseDto.setTotalRealPriceOfShop(adminTurnoverTotalShopResponseDto.getTotalRealPrice().subtract(adminTurnoverTotalShopResponseDto.getTotalRealPriceOfAdmin()));
        }
        return adminTurnoverTotalShopResponseDtos;
    }
    @Override
    @Transactional(readOnly = true)
    public AdminTurnoverTotalShopResponseDto getAllRevenue(Long dateFrom, Long dateTo) {
        Instant localDateFrom = Instant.ofEpochSecond(dateFrom).truncatedTo(ChronoUnit.DAYS);

        Instant localDateTo = Instant.ofEpochSecond(dateTo).plus(1, ChronoUnit.DAYS).truncatedTo(ChronoUnit.DAYS);
        List<AdminTurnoverTotalShopResponseDto> adminTurnoverTotalShopResponseDtos = billRepository.getAllTotalTurnoverShop(localDateFrom, localDateTo,BillStatus.DONE);
        Map<Long, BigDecimal> adminTurnoverDiscountShopDtos = billRepository.getAllTotalVoucherAdmin(VoucherProvider.ADMIN, localDateFrom, localDateTo, BillStatus.DONE).stream().collect(Collectors.toMap(AdminTurnoverDiscountShopDto::getShopId, AdminTurnoverDiscountShopDto::getDiscountAdmin));
        for (AdminTurnoverTotalShopResponseDto adminTurnoverTotalShopResponseDto : adminTurnoverTotalShopResponseDtos) {
            if (null == adminTurnoverTotalShopResponseDto) {
                adminTurnoverTotalShopResponseDto = new AdminTurnoverTotalShopResponseDto();
            }
            adminTurnoverTotalShopResponseDto.setTotalPriceDiscountOfAdmin(adminTurnoverDiscountShopDtos.get(adminTurnoverTotalShopResponseDto.getShopId()));
            if (null == adminTurnoverTotalShopResponseDto.getTotalPriceDiscountOfAdmin()) {
                adminTurnoverTotalShopResponseDto.setTotalPriceDiscountOfAdmin(BigDecimal.valueOf(0l));
            }
            adminTurnoverTotalShopResponseDto.setTotalPriceDiscountOfShop(adminTurnoverTotalShopResponseDto.getTotalPriceDiscount().subtract(adminTurnoverTotalShopResponseDto.getTotalPriceDiscountOfAdmin()));
            adminTurnoverTotalShopResponseDto.setTotalRealPriceOfAdmin(adminTurnoverTotalShopResponseDto.getTotalRealPrice().multiply(BigDecimal.valueOf(5l)).divide(BigDecimal.valueOf(100)).subtract(adminTurnoverTotalShopResponseDto.getTotalPriceDiscountOfAdmin()));
            adminTurnoverTotalShopResponseDto.setTotalRealPriceOfShop(adminTurnoverTotalShopResponseDto.getTotalRealPrice().subtract(adminTurnoverTotalShopResponseDto.getTotalRealPriceOfAdmin()));
        }

        AdminTurnoverTotalShopResponseDto allAdminTurnoverTotalShopResponseDto = new AdminTurnoverTotalShopResponseDto();
        allAdminTurnoverTotalShopResponseDto.setTotalPriceDiscount(BigDecimal.valueOf(0l));
        allAdminTurnoverTotalShopResponseDto.setTotalPriceDiscountOfAdmin(BigDecimal.valueOf(0l));
        allAdminTurnoverTotalShopResponseDto.setTotalPriceDiscountOfShop(BigDecimal.valueOf(0l));
        allAdminTurnoverTotalShopResponseDto.setTotalPriceProductOrigin(BigDecimal.valueOf(0l));
        allAdminTurnoverTotalShopResponseDto.setTotalRealPrice(BigDecimal.valueOf(0l));
        allAdminTurnoverTotalShopResponseDto.setTotalRealPriceOfAdmin(BigDecimal.valueOf(0l));
        allAdminTurnoverTotalShopResponseDto.setTotalRealPriceOfShop(BigDecimal.valueOf(0l));
        for (AdminTurnoverTotalShopResponseDto adminTurnoverTotalShopResponseDto : adminTurnoverTotalShopResponseDtos) {
            allAdminTurnoverTotalShopResponseDto.setTotalPriceDiscount(allAdminTurnoverTotalShopResponseDto.getTotalPriceDiscount().add(adminTurnoverTotalShopResponseDto.getTotalPriceDiscount()));
            allAdminTurnoverTotalShopResponseDto.setTotalPriceDiscountOfAdmin(allAdminTurnoverTotalShopResponseDto.getTotalPriceDiscountOfAdmin().add(adminTurnoverTotalShopResponseDto.getTotalPriceDiscountOfAdmin()));
            allAdminTurnoverTotalShopResponseDto.setTotalPriceDiscountOfShop(allAdminTurnoverTotalShopResponseDto.getTotalPriceDiscountOfShop().add(adminTurnoverTotalShopResponseDto.getTotalPriceDiscountOfShop()));
            allAdminTurnoverTotalShopResponseDto.setTotalPriceProductOrigin(allAdminTurnoverTotalShopResponseDto.getTotalPriceProductOrigin().add(adminTurnoverTotalShopResponseDto.getTotalPriceProductOrigin()));
            allAdminTurnoverTotalShopResponseDto.setTotalRealPrice(allAdminTurnoverTotalShopResponseDto.getTotalRealPrice().add(adminTurnoverTotalShopResponseDto.getTotalRealPrice()));
            allAdminTurnoverTotalShopResponseDto.setTotalRealPriceOfAdmin(allAdminTurnoverTotalShopResponseDto.getTotalRealPriceOfAdmin().add(adminTurnoverTotalShopResponseDto.getTotalRealPriceOfAdmin()));
            allAdminTurnoverTotalShopResponseDto.setTotalRealPriceOfShop(allAdminTurnoverTotalShopResponseDto.getTotalRealPriceOfShop().add(adminTurnoverTotalShopResponseDto.getTotalRealPriceOfShop()));

        }
        return allAdminTurnoverTotalShopResponseDto;
    }

    @Override
    @Transactional(readOnly = true)
    public AdminTurnoverTotalShopResponseDto getRevenueShop(Long dateFrom, Long dateTo, Long shopId) {
        Instant localDateFrom = Instant.ofEpochSecond(dateFrom).truncatedTo(ChronoUnit.DAYS);

        Instant localDateTo = Instant.ofEpochSecond(dateTo).plus(1, ChronoUnit.DAYS).truncatedTo(ChronoUnit.DAYS);
//        AdminTurnoverTotalShopResponseDto adminTurnoverTotalShopResponseDto = billRepository.getTurnoverShop(localDateFrom, localDateTo, shopId);

        AdminTurnoverTotalShopResponseDto adminTurnoverTotalShopResponseDto = billRepository.getTurnoverShop(localDateFrom, localDateTo, shopId, BillStatus.DONE);
        if (null == adminTurnoverTotalShopResponseDto) {
            adminTurnoverTotalShopResponseDto = new AdminTurnoverTotalShopResponseDto();
        }
        if (null == adminTurnoverTotalShopResponseDto.getTotalPriceDiscountOfAdmin()) {
            adminTurnoverTotalShopResponseDto.setTotalPriceDiscountOfAdmin(BigDecimal.valueOf(0l));
        }else
        adminTurnoverTotalShopResponseDto.setTotalPriceDiscountOfAdmin(billRepository.getTotalVoucher(VoucherProvider.ADMIN, localDateFrom, localDateTo, shopId, BillStatus.DONE));
        if(null ==adminTurnoverTotalShopResponseDto.getTotalPriceDiscount()){
            adminTurnoverTotalShopResponseDto.setTotalPriceDiscount(BigDecimal.valueOf(0));
        }
        if(null ==adminTurnoverTotalShopResponseDto.getTotalRealPrice()){
            adminTurnoverTotalShopResponseDto.setTotalRealPrice(BigDecimal.valueOf(0));
        }
        if(null ==adminTurnoverTotalShopResponseDto.getTotalRealPriceOfShop()){
            adminTurnoverTotalShopResponseDto.setTotalRealPriceOfShop(BigDecimal.valueOf(0));
        }
        adminTurnoverTotalShopResponseDto.setTotalPriceDiscountOfShop(adminTurnoverTotalShopResponseDto.getTotalPriceDiscount().subtract(adminTurnoverTotalShopResponseDto.getTotalPriceDiscountOfAdmin()));
        adminTurnoverTotalShopResponseDto.setTotalRealPriceOfAdmin(adminTurnoverTotalShopResponseDto.getTotalRealPrice().multiply(BigDecimal.valueOf(5l)).divide(BigDecimal.valueOf(100)).subtract(adminTurnoverTotalShopResponseDto.getTotalPriceDiscountOfAdmin()));
        adminTurnoverTotalShopResponseDto.setTotalRealPriceOfShop(adminTurnoverTotalShopResponseDto.getTotalRealPrice().subtract(adminTurnoverTotalShopResponseDto.getTotalRealPriceOfAdmin()));
        return adminTurnoverTotalShopResponseDto;
    }
}
