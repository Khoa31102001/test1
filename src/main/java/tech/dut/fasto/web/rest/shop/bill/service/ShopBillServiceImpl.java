package tech.dut.fasto.web.rest.shop.bill.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.dut.fasto.common.domain.Bill;
import tech.dut.fasto.common.domain.Payment;
import tech.dut.fasto.common.domain.Shop;
import tech.dut.fasto.common.domain.enumeration.BillStatus;
import tech.dut.fasto.common.domain.enumeration.UserStatus;
import tech.dut.fasto.common.repository.BillItemRepository;
import tech.dut.fasto.common.repository.BillRepository;
import tech.dut.fasto.common.repository.PaymentRepository;
import tech.dut.fasto.common.repository.ShopRepository;

import tech.dut.fasto.common.service.impl.MessageService;
import tech.dut.fasto.config.security.SecurityUtils;
import tech.dut.fasto.errors.FastoAlertException;
import tech.dut.fasto.errors.FastoAlertException;
import tech.dut.fasto.web.rest.shop.bill.dto.response.BillCodeResponseDto;
import tech.dut.fasto.web.rest.shop.bill.dto.response.BillDetailResponseDto;
import tech.dut.fasto.web.rest.shop.bill.dto.response.BillUserResponseDto;
import tech.dut.fasto.web.rest.shop.billitem.dto.response.BillItemResponseDto;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ShopBillServiceImpl implements ShopBillService {

    private final BillRepository billRepository;

    private final ShopRepository shopRepository;

    private final PaymentRepository paymentRepository;

    private final BillItemRepository billItemRepository;

    private final EntityManagerFactory entityManagerFactory;

    private final MessageService messageService;

    @Override
    @Transactional(readOnly = true)
    public Page<BillUserResponseDto> getAllListBill(BillStatus status, String query, Pageable pageable) {
        
        String email = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.bill.shop.get.failed"), messageService.getMessage("error.authenticate.unauthorized.user")));
        Shop shop = shopRepository.findByUserEmailAndUserStatus(email, UserStatus.ACTIVATED).orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.bill.shop.get.failed"), messageService.getMessage("error.shop.is.inactive")));

        return billRepository.getAllByShopId(shop.getId(), status, query, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public BillDetailResponseDto getDetailBill(Long billId) {
        Optional<Bill> optionalBill = billRepository.findById(billId);
        if (optionalBill.isEmpty()) {
            throw new FastoAlertException(messageService.getMessage("error.code.bill.shop.get.detail.failed"), messageService.getMessage("error.bill.not.found"));
        }

        Bill bill = optionalBill.get();

        Optional<Payment> optionalPayment = paymentRepository.findByBillId(billId);
        Payment payment = null;
        if (optionalPayment.isPresent()) {
            payment = optionalPayment.get();
        }
        List<BillItemResponseDto> billItemResponseDtos = billItemRepository.getAllBillItemByBillId(billId);
        BillDetailResponseDto billDetailResponseDto = new BillDetailResponseDto();
        billDetailResponseDto.setBillId(bill.getId());
        billDetailResponseDto.setBillItemResponseDtos(billItemResponseDtos);
        billDetailResponseDto.setTotalOrigin(bill.getTotalOrigin());
        billDetailResponseDto.setTotalPayment(bill.getTotalPayment());
        billDetailResponseDto.setTotalDiscount(bill.getTotalVoucher());
        billDetailResponseDto.setStatus(bill.getStatus());
        billDetailResponseDto.setShippingFee(bill.getShippingFee());
        if (payment != null) {
            billDetailResponseDto.setPaymentType(payment.getType());
        }
        billDetailResponseDto.setUserId(bill.getUser().getId());
        billDetailResponseDto.setUserFirstName(bill.getUser().getUserInformation().getFirstName());
        billDetailResponseDto.setUserLastName(bill.getUser().getUserInformation().getLastName());
        billDetailResponseDto.setCreatedAt(bill.getCreatedAt());
        if (null != bill.getVoucher()) {
            billDetailResponseDto.setVoucherId(bill.getVoucher().getId());
            billDetailResponseDto.setVoucherName(bill.getVoucher().getName());
            billDetailResponseDto.setVoucherStartAt(bill.getVoucher().getStartedAt());
            billDetailResponseDto.setVoucherEndedAt(bill.getVoucher().getEndedAt());
            billDetailResponseDto.setUserType(bill.getVoucher().getUserType());
            billDetailResponseDto.setVoucherType(bill.getVoucher().getVoucherType());
            billDetailResponseDto.setValueNeed(bill.getVoucher().getValueNeed());
            billDetailResponseDto.setVoucherDiscountImage(bill.getVoucher().getImage());
            billDetailResponseDto.setValueDiscount(bill.getVoucher().getValueDiscount());
        }
        billDetailResponseDto.setIsRating(bill.getIsRating());
        billDetailResponseDto.setRating(bill.getRatings());
        billDetailResponseDto.setUserImage(bill.getUser().getUserInformation().getUserImage());
        return billDetailResponseDto;
    }

//    @Override
//    @Transactional(readOnly = true)
//    public BillDetailResponseDto getBillByCode(String code) {
//        String email = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.bill.code.is.not.correct"), messageService.getMessage("error.authenticate.unauthorized.user")));
//        Shop shop = shopRepository.findByUserEmailAndUserStatus(email, UserStatus.ACTIVATED).orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.bill.code.is.not.correct"), messageService.getMessage("error.shop.is.inactive")));
//
//        Optional<Bill> optionalBill = billRepository.findByShopIdAndCode(shop.getId(),code);
//        if (optionalBill.isEmpty()) {
//            throw new FastoAlertException(messageService.getMessage("error.code.bill.code.is.not.correct"), messageService.getMessage("error.bill.not.found"));
//        }
//        Bill bill = optionalBill.get();
//        Optional<Payment> optionalPayment = paymentRepository.findByBillId(bill.getId());
//        Payment payment = null;
//        if (optionalPayment.isPresent()) {
//            payment = optionalPayment.get();
//        }
//        List<BillItemResponseDto> billItemResponseDtos = billItemRepository.getAllBillItemByBillId(bill.getId());
//        BillDetailResponseDto billDetailResponseDto = new BillDetailResponseDto();
//        billDetailResponseDto.setBillId(bill.getId());
//        billDetailResponseDto.setBillItemResponseDtos(billItemResponseDtos);
//        billDetailResponseDto.setTotalOrigin(bill.getTotalOrigin());
//        billDetailResponseDto.setTotalPayment(bill.getTotalPayment());
//        billDetailResponseDto.setTotalDiscount(bill.getTotalVoucher());
//
//        billDetailResponseDto.setStatus(bill.getStatus());
//        if (payment != null) {
//            billDetailResponseDto.setPaymentType(payment.getType());
//        }
//        billDetailResponseDto.setUserId(bill.getUser().getId());
//        billDetailResponseDto.setUserFirstName(bill.getUser().getUserInformation().getFirstName());
//        billDetailResponseDto.setUserLastName(bill.getUser().getUserInformation().getLastName());
//        billDetailResponseDto.setCreatedAt(bill.getCreatedAt());
//        if (null != bill.getVoucher()) {
//            billDetailResponseDto.setVoucherId(bill.getVoucher().getId());
//            billDetailResponseDto.setVoucherName(bill.getVoucher().getName());
//            billDetailResponseDto.setVoucherStartAt(bill.getVoucher().getStartedAt());
//            billDetailResponseDto.setVoucherEndedAt(bill.getVoucher().getEndedAt());
//            billDetailResponseDto.setUserType(bill.getVoucher().getUserType());
//            billDetailResponseDto.setVoucherType(bill.getVoucher().getVoucherType());
//            billDetailResponseDto.setVoucherDiscountImage(bill.getVoucher().getImage());
//            billDetailResponseDto.setValueNeed(bill.getVoucher().getValueNeed());
//            billDetailResponseDto.setValueDiscount(bill.getVoucher().getValueDiscount());
//        }
//        billDetailResponseDto.setIsRating(bill.getIsRating());
//        billDetailResponseDto.setRating(bill.getRatings());
//        billDetailResponseDto.setUserImage(bill.getUser().getUserInformation().getUserImage());
//        return billDetailResponseDto;
//    }

//    @Override
//    @Transactional(readOnly = true)
//    public BillCodeResponseDto getCodeBill(Long billId) {
//        String email = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.bill.code.is.not.correct"), messageService.getMessage("error.authenticate.unauthorized.user")));
//        Shop shop = shopRepository.findByUserEmailAndUserStatus(email, UserStatus.ACTIVATED).orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.bill.code.is.not.correct"), messageService.getMessage("error.shop.is.inactive")));
//
//        EntityManager entityManager = entityManagerFactory.createEntityManager();
//
//        Instant instant = Instant.now().truncatedTo(ChronoUnit.DAYS);
//
//        TypedQuery<BillCodeResponseDto> query = entityManager.createNamedQuery("getCodeBill", BillCodeResponseDto.class).setParameter("id", billId).setParameter("shopId", shop.getId()).setParameter("status", BillStatus.PAID).setParameter("expiredCode", instant);
//        return query.getSingleResult();
//    }

    @Override
    @Transactional(rollbackFor = FastoAlertException.class)
    public void validCodeForBill(Long id) {
        String email = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.bill.code.is.not.correct"), messageService.getMessage("error.authenticate.unauthorized.user")));
        Shop shop = shopRepository.findByUserEmailAndUserStatus(email, UserStatus.ACTIVATED).orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.bill.code.is.not.correct"), messageService.getMessage("error.shop.is.inactive")));

        Optional<Bill> optionalBill = billRepository.findByIdAndShopId(id, shop.getId());
        if (optionalBill.isPresent()) {
            Bill bill = optionalBill.get();
            bill.setStatus(BillStatus.DONE);
        } else {
            throw new FastoAlertException(messageService.getMessage("error.code.bill.valid.failed"), messageService.getMessage("error.bill.code.is.invalid"));
        }
    }

}
