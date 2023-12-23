package tech.dut.fasto.web.rest.user.bill.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.dut.fasto.common.domain.*;
import tech.dut.fasto.common.domain.enumeration.*;
import tech.dut.fasto.common.repository.*;

import tech.dut.fasto.common.service.impl.MessageService;
import tech.dut.fasto.config.security.SecurityUtils;
import tech.dut.fasto.errors.FastoAlertException;
import tech.dut.fasto.errors.FastoAlertException;
import tech.dut.fasto.util.AppUtil;
import tech.dut.fasto.web.rest.shop.bill.dto.response.BillDetailResponseDto;
import tech.dut.fasto.web.rest.shop.billitem.dto.response.BillItemResponseDto;
import tech.dut.fasto.web.rest.user.bill.dto.request.BillProductDetailRequestDto;
import tech.dut.fasto.web.rest.user.bill.dto.request.BillProductRequestDto;
import tech.dut.fasto.web.rest.user.bill.dto.response.BillProductResponseDto;
import tech.dut.fasto.web.rest.user.bill.dto.response.BillResponseDto;
import tech.dut.fasto.web.rest.user.bill.dto.response.ShippingFeeResponseDto;
import tech.dut.fasto.web.rest.user.bill.dto.response.UserBillResponseDto;
import tech.dut.fasto.web.rest.user.cart.service.CartService;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserBillServiceImpl implements UserBillService {

    private final BillRepository billRepository;
    private final UserRepository userRepository;

    private final BillItemRepository billItemRepository;

    private final VoucherRepository voucherRepository;

    private final VoucherUserRepository voucherUserRepository;

    private final PaymentRepository paymentRepository;

    private final ProductRepository productRepository;

    private final ShopRepository shopRepository;

    private final CartService cartService;

    private final MessageService messageService;

    @Override
    @Transactional(rollbackFor = FastoAlertException.class)
    public BillResponseDto createBill(BillProductRequestDto billProductRequestDto) {
        String email = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.bill.create.failed"), messageService.getMessage("error.authenticate.unauthorized.user")));
        User user = userRepository.findByEmailAndStatus(email, UserStatus.ACTIVATED).orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.bill.create.failed"), messageService.getMessage("error.authenticate.user.already.activated")));
//        Optional<Bill> optionalBill = billRepository.findByUserIdAndStatus(user.getId(), BillStatus.CREATED);
//        if (optionalBill.isPresent()) {
//            Bill tmp = optionalBill.get();
//            tmp.setStatus(BillStatus.FAILED);
//        }

        Map<Long, BillProductDetailRequestDto> maps = billProductRequestDto.getProductDetailRequestDtos().stream().collect(Collectors.toMap(BillProductDetailRequestDto::getProductId, Function.identity()));
        List<Long> ids = billProductRequestDto.getProductDetailRequestDtos().stream().map(BillProductDetailRequestDto::getProductId).collect(Collectors.toList());
        List<Product> products = productRepository.getProductInIds(ids);
        BigDecimal totalOrigin = BigDecimal.valueOf(0L);
        List<BillItem> billItems = new ArrayList<>();

        for (Product product : products) {
            if (!product.getShop().getId().equals(billProductRequestDto.getShopId())) {
                throw new FastoAlertException(messageService.getMessage("error.code.bill.create.failed"), messageService.getMessage("error.product.not.found"));
            }
            BillProductDetailRequestDto productDetailRequestDto = maps.get(product.getId());
            BigDecimal tmp = product.getPrice().multiply(new BigDecimal(productDetailRequestDto.getAmount()));
            totalOrigin = totalOrigin.add(tmp);
        }

        for (Product item : products) {
            BillItem billItem = new BillItem();
            billItem.setAmount(maps.get(item.getId()).getAmount());
            billItem.setProduct(item);
            billItem.setUser(user);
            billItem.setShop(item.getShop());

            billItems.add(billItem);
        }
        Optional<Voucher> optionalVoucher = voucherRepository.findByIdAndShopIdAndStatusNot(billProductRequestDto.getVoucherId(), billProductRequestDto.getShopId(), VoucherStatus.DELETED);
        Optional<Voucher> voucherOptionalAdmin = voucherRepository.findByIdAndUserType(billProductRequestDto.getVoucherId(), VoucherProvider.ADMIN);
        Voucher voucher = null;

        if (optionalVoucher.isPresent()) {
            voucher = optionalVoucher.get();
        }

        if (voucherOptionalAdmin.isPresent()) {
            voucher = voucherOptionalAdmin.get();
        }

        if (voucher != null) {
            if (voucher.getEndedAt().truncatedTo(ChronoUnit.DAYS).toEpochMilli() <= Instant.now().truncatedTo(ChronoUnit.DAYS).toEpochMilli()) {
                throw new FastoAlertException(messageService.getMessage("error.code.bill.create.failed"), messageService.getMessage("error.voucher.expired"));
            }
            if (voucher.getValueNeed().compareTo(totalOrigin) > 0) {
                throw new FastoAlertException(messageService.getMessage("error.code.bill.create.failed"), messageService.getMessage("error.voucher.total.origin.less.than.value.need"));
            }

            if (voucher.getQuantity() <= 0) {
                throw new FastoAlertException(messageService.getMessage("error.code.bill.create.failed"), messageService.getMessage("error.voucher.unavailable"));
            }
        }

        if (voucher != null) {
            if (!voucherUserRepository.existsByUserIdAndVoucherId(user.getId(), billProductRequestDto.getVoucherId())) {
                VoucherUser voucherUser = new VoucherUser();
                voucherUser.setVoucher(voucher);
                voucherUser.setUser(user);
                voucherUser.setUsed(1);
                voucherUserRepository.save(voucherUser);
                voucher.setQuantity(voucher.getQuantity() - 1L);
            } else {
                VoucherUser voucherUser = voucherUserRepository.findByUserIdAndVoucherId(user.getId(), billProductRequestDto.getVoucherId()).orElseThrow(() -> new FastoAlertException("Create bill failed", messageService.getMessage("error.voucher.not.found")));
                if (voucher.getLimitPerUser().toString().equals(voucherUser.getUsed().toString())) {
                    throw new FastoAlertException(messageService.getMessage("error.code.bill.create.failed"), messageService.getMessage("error.voucher.user.use.reach.limit"));
                }
                voucherUser.setUsed(voucherUser.getUsed() + 1);
                voucher.setQuantity(voucher.getQuantity() - 1);
            }
        }


        BigDecimal totalVoucher = new BigDecimal(0);
        BigDecimal totalPayment = null;

        if (voucher != null) {
            if (!voucher.getVoucherType().equals(VoucherType.PERCENT)) {
                totalVoucher = voucher.getValueDiscount();
            } else {
                totalVoucher = totalOrigin.multiply(voucher.getValueDiscount()).divide(new BigDecimal(100L));
                if (totalVoucher.compareTo(voucher.getMaxDiscount()) > 0) {
                    totalVoucher = voucher.getMaxDiscount();
                }
            }
        }

        totalPayment = totalOrigin.subtract(totalVoucher);

        Shop shop = shopRepository.findByIdAndUserStatus(billProductRequestDto.getShopId(), UserStatus.ACTIVATED).orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.bill.create.failed"), messageService.getMessage("error.shop.not.found")));
        Double shippingKm = AppUtil.getDistanceFromLatLonInKm(billProductRequestDto.getX(), billProductRequestDto.getY(), shop.getAddress().getX(), shop.getAddress().getY());
        BigDecimal shippingFee;
        if(shippingKm<=3D){
            shippingFee = BigDecimal.valueOf(15000l);
        }else{
            Long tmpFee =Long.valueOf(15000+(shippingKm.longValue()-3)*5000);
            shippingFee = BigDecimal.valueOf(tmpFee);
        }

        Bill bill = new Bill();
        bill.setUser(user);
        bill.setShop(shop);
        bill.setTotalOrigin(totalOrigin);
        bill.setTotalPayment(totalPayment);
        bill.setTotalVoucher(totalVoucher);
        bill.setShippingFee(shippingFee);
        bill.setStatus(BillStatus.CREATED);
        if (voucher != null) {
            bill.setVoucher(voucher);
        }


        for (BillItem billItem : billItems) {
            billItem.setBill(bill);
        }


        billRepository.save(bill);
        billItemRepository.saveAll(billItems);

        cartService.removeProductCart(billProductRequestDto.getProductDetailRequestDtos().stream().map(BillProductDetailRequestDto::getProductId).toList(), billProductRequestDto.getShopId(), billProductRequestDto.getDeviceToken());

        List<BillProductResponseDto> productResponseDtos = productRepository.getProductResponseInIds(ids);
        BillResponseDto billResponseDto = new BillResponseDto();
        billResponseDto.setBillId(bill.getId());
        billResponseDto.setProductResponseDtos(productResponseDtos);
        billResponseDto.setTotalOrigin(bill.getTotalOrigin());
        billResponseDto.setTotalDiscount(bill.getTotalVoucher());
        billResponseDto.setTotalPayment(bill.getTotalPayment());
        billResponseDto.setShippingFee(shippingFee);
        return billResponseDto;

    }

    @Override
    @Transactional(readOnly = true)
    public List<UserBillResponseDto> getAllListBill(BillStatus status, String query) {

        String email = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.bill.get.all.failed"), messageService.getMessage("error.authenticate.unauthorized.user")));
        User user = userRepository.findByEmailAndStatus(email, UserStatus.ACTIVATED).orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.bill.get.all.failed"),  messageService.getMessage("error.authenticate.user.not.activated")));
        return billRepository.getAllByUserIdAndStatus(user.getId(), status, query);
    }

    @Override
    @Transactional(readOnly = true)
    public BillDetailResponseDto getDetailBill(Long billId) {
        Optional<Bill> optionalBill = billRepository.findById(billId);
        if (optionalBill.isEmpty()) {
            throw new FastoAlertException(messageService.getMessage("error.code.bill.get.detail.failed"), messageService.getMessage("error.bill.not.found"));
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
        billDetailResponseDto.setShopId(bill.getShop().getId());
        billDetailResponseDto.setShopName(bill.getShop().getName());
        billDetailResponseDto.setLogo(bill.getShop().getBanner());
        billDetailResponseDto.setAddressId(bill.getShop().getAddress().getId());
        billDetailResponseDto.setCity(bill.getShop().getAddress().getCity());
        billDetailResponseDto.setDistrict(bill.getShop().getAddress().getStateProvince());
        billDetailResponseDto.setStreet(bill.getShop().getAddress().getStreetAddress());
        billDetailResponseDto.setX(bill.getShop().getAddress().getX());
        billDetailResponseDto.setY(bill.getShop().getAddress().getY());

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

        billDetailResponseDto.setRating(bill.getRatings());
        billDetailResponseDto.setIsRating(bill.getIsRating());

        return billDetailResponseDto;
    }

    @Override
    @Transactional(readOnly = true)
    public String proceedPayment(Long id) {
        Bill bill = billRepository.findByIdAndStatus(id, BillStatus.PENDING).orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.bill.get.detail.failed"), messageService.getMessage("error.bill.not.found")));
        if(bill.getExpiredTime().isBefore(Instant.now())){
            throw new FastoAlertException(messageService.getMessage("error.code.bill.get.detail.failed"), messageService.getMessage("error.bill.not.found"));
        }
        return bill.getUrlPayment();
    }

    @Override
    @Transactional(rollbackFor = FastoAlertException.class)
    public void doneBill(Long id) {
        Bill bill = billRepository.findByIdAndStatus(id, BillStatus.PAID).orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.bill.get.detail.failed"), messageService.getMessage("error.bill.not.found")));
        bill.setStatus(BillStatus.DONE);

    }

    @Override
    @Transactional(rollbackFor = FastoAlertException.class)
    public ShippingFeeResponseDto calculateShippingFee(Long shopId, Double x, Double y) {
        Shop shop = shopRepository.findByIdAndUserStatus(shopId, UserStatus.ACTIVATED).orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.bill.create.failed"), messageService.getMessage("error.shop.not.found")));
        Double shippingKm = AppUtil.getDistanceFromLatLonInKm(x,y, shop.getAddress().getX(), shop.getAddress().getY());
        BigDecimal shippingFee;
        if(shippingKm<=3D){
            shippingFee = BigDecimal.valueOf(15000l);
        }else{
            Long tmpFee =Long.valueOf(15000+(shippingKm.longValue()-3)*5000);
            shippingFee = BigDecimal.valueOf(tmpFee);
        }
        ShippingFeeResponseDto shippingFeeResponseDto = new ShippingFeeResponseDto();
        shippingFeeResponseDto.setShippingFee(shippingFee);
        return shippingFeeResponseDto;
    }

}
