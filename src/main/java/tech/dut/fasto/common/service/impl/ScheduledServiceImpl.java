package tech.dut.fasto.common.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.dut.fasto.common.domain.Bill;
import tech.dut.fasto.common.domain.Shop;
import tech.dut.fasto.common.domain.Voucher;
import tech.dut.fasto.common.domain.enumeration.BillStatus;
import tech.dut.fasto.common.domain.enumeration.ShopSchedule;
import tech.dut.fasto.common.domain.enumeration.UserStatus;
import tech.dut.fasto.common.domain.enumeration.VoucherStatus;
import tech.dut.fasto.common.repository.BillRepository;
import tech.dut.fasto.common.repository.ShopRepository;
import tech.dut.fasto.common.repository.VoucherRepository;
import tech.dut.fasto.common.service.ScheduledService;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduledServiceImpl implements ScheduledService {

    private final ShopRepository shopRepository;

    private final VoucherRepository voucherRepository;

    private final BillRepository billRepository;

    @Override
    @Transactional
    @Async
    @Scheduled(cron = "0 0 7,22 * * *", zone = "Asia/Ho_Chi_Minh")
    public void scheduleStatusTimeShop() {
        List<Shop> shops = shopRepository.findAllByUserStatus(UserStatus.ACTIVATED);
        for (Shop shop : shops) {
            if (shop.getScheduleActive().equals(ShopSchedule.OPEN)) {
                shop.setScheduleActive(ShopSchedule.CLOSE);
            } else {
                shop.setScheduleActive(ShopSchedule.OPEN);
            }

        }
    }

    @Override
    @Transactional
    @Async
    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Ho_Chi_Minh")
    public void scheduleStatusTimeVoucher() {

        List<Voucher> vouchers = voucherRepository.findAll();
        for (Voucher voucher : vouchers) {

            if (voucher.getStartedAt().truncatedTo(ChronoUnit.DAYS).isAfter(Instant.now().truncatedTo(ChronoUnit.DAYS)) || voucher.getEndedAt().truncatedTo(ChronoUnit.DAYS).isBefore(Instant.now().truncatedTo(ChronoUnit.DAYS))) {
                voucher.setStatus(VoucherStatus.INACTIVE);
            }else {
                voucher.setStatus(VoucherStatus.ACTIVATED);
            }
        }
    }

    @Override
    @Transactional
    @Async
    @Scheduled(cron = "0 0/1 * * * *", zone = "Asia/Ho_Chi_Minh")
    public void scheduleStatusTimeBill() {
        List<Bill> bills = billRepository.findAll();
        for(Bill bill : bills) {
            if(Instant.now().isAfter(bill.getExpiredTime())&& BillStatus.PENDING.equals(bill.getStatus())) {
                bill.setStatus(BillStatus.FAILED);
                bill.setUrlPayment(null);
            }
        }
    }
}
