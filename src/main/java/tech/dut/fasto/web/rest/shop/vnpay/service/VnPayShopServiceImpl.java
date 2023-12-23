package tech.dut.fasto.web.rest.shop.vnpay.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.dut.fasto.common.domain.Bill;
import tech.dut.fasto.common.domain.Shop;
import tech.dut.fasto.common.domain.User;
import tech.dut.fasto.common.domain.enumeration.BillStatus;
import tech.dut.fasto.common.domain.enumeration.UserStatus;
import tech.dut.fasto.common.repository.BillRepository;
import tech.dut.fasto.common.repository.ShopRepository;
import tech.dut.fasto.common.repository.UserRepository;
import tech.dut.fasto.common.service.impl.MessageService;
import tech.dut.fasto.config.properties.FastoProperties;
import tech.dut.fasto.config.security.SecurityUtils;
import tech.dut.fasto.errors.FastoAlertException;
import tech.dut.fasto.util.VnPayUtil;
import tech.dut.fasto.util.constants.Constants;
import tech.dut.fasto.web.rest.shop.vnpay.dto.request.VNPayCreateUrlRequestShopDto;
import tech.dut.fasto.web.rest.shop.vnpay.dto.response.VNPayCreateUrlResponseShopDto;
import tech.dut.fasto.web.rest.user.vnpay.dto.request.VNPayCreateUrlRequestDto;
import tech.dut.fasto.web.rest.user.vnpay.dto.response.VNPayCreateUrlResponseDto;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
public class VnPayShopServiceImpl implements VnPayShopService {
    private final BillRepository billRepository;
    private final ShopRepository shopRepository;
    private final UserRepository userRepository;
    private final MessageService messageService;
    private final FastoProperties fastoProperties;

    private final BigDecimal DEPOSIT = BigDecimal.valueOf(5000000l);


    @Override
    @Transactional(rollbackFor = FastoAlertException.class)
    public VNPayCreateUrlResponseShopDto depositForSystem(VNPayCreateUrlRequestShopDto requestDTO, HttpServletRequest request) {

        String email = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.user.get.information.failed"), messageService.getMessage("error.authenticate.unauthorized.user")));
        Shop shop = shopRepository.findByUserEmailAndUserStatus(email, UserStatus.ACTIVATED).orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.bill.shop.get.failed"), messageService.getMessage("error.shop.is.inactive")));
        User user = userRepository.findByEmailAndStatus(email, UserStatus.ACTIVATED).orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.user.get.information.failed"), messageService.getMessage("error.authenticate.unauthorized.user")));

        Optional<Bill> optionalBill = billRepository.findByUserIdAndShopIdAndStatus(user.getId(),shop.getId(),BillStatus.PENDING);
        Bill bill = null;
        if(optionalBill.isPresent()){
            bill = optionalBill.get();
        }else{
            bill = new Bill();
            bill.setShop(shop);
            bill.setUser(user);
            bill.setTotalOrigin(DEPOSIT);
            bill.setTotalVoucher(BigDecimal.valueOf(0));
            bill.setTotalPayment(DEPOSIT);
            bill.setStatus(BillStatus.PENDING);
            billRepository.saveAndFlush(bill);
        }
        String ipAdd = VnPayUtil.getIpAddress(request);
        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.VNP_DATE_FORMAT);

        String orderInfo = "Thanh toan cho tien coc cua hoa don " + bill.getId();
        String vnpCreateDate = formatter.format(zonedDateTime);

        String vnpTxnRef = "deposit" + "_" + shop.getId() + "_" +"bill"+ "_" +bill.getId();

        zonedDateTime = zonedDateTime.plusMinutes(15);
        String vnpExpireDate = formatter.format(zonedDateTime);

        Map<String, String> vnpParams = new HashMap<>();
        vnpParams.put("vnp_Version", fastoProperties.getVnPay().getVersion());
        vnpParams.put("vnp_Command", fastoProperties.getVnPay().getCommand());
        vnpParams.put("vnp_TmnCode", fastoProperties.getVnPay().getTmnCode());
        vnpParams.put("vnp_Amount", String.valueOf(bill.getTotalPayment().multiply(BigDecimal.valueOf(100L))));
        vnpParams.put("vnp_CurrCode", fastoProperties.getVnPay().getCurrCode());
        vnpParams.put("vnp_BankCode", requestDTO.getBankCode());
        vnpParams.put("vnp_TxnRef", vnpTxnRef);
        vnpParams.put("vnp_OrderInfo", orderInfo);
        vnpParams.put("vnp_OrderType", requestDTO.getVnpOrderType());
        vnpParams.put("vnp_Locale", requestDTO.getVnpLocale());
        vnpParams.put("vnp_ReturnUrl", requestDTO.getVnpReturnUrl());
        vnpParams.put("vnp_IpAddr", ipAdd);
        vnpParams.put("vnp_CreateDate", vnpCreateDate);
        vnpParams.put("vnp_ExpireDate", vnpExpireDate);

        List fieldNames = new ArrayList(vnpParams.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = vnpParams.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnpSecureHash = VnPayUtil.hmacSHA512(fastoProperties.getVnPay().getHashSecret(), hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnpSecureHash;
        String paymentUrl = fastoProperties.getVnPay().getPayUrl() + "?" + queryUrl;
        VNPayCreateUrlResponseShopDto responseDTO = new VNPayCreateUrlResponseShopDto();
        responseDTO.setUrl(paymentUrl);
        responseDTO.setBillId(bill.getId());
        bill.setExpiredTime(Instant.now().plus(15, ChronoUnit.MINUTES));
        bill.setUrlPayment(paymentUrl);

        return responseDTO;
    }
}
