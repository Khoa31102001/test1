package tech.dut.fasto.web.rest.user.vnpay.service;

import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.dut.fasto.common.domain.*;
import tech.dut.fasto.common.domain.enumeration.*;
import tech.dut.fasto.common.dto.NotificationDto;
import tech.dut.fasto.common.repository.*;

import tech.dut.fasto.common.service.EmailService;
import tech.dut.fasto.common.service.NotificationService;
import tech.dut.fasto.common.service.impl.MessageService;
import tech.dut.fasto.config.properties.FastoProperties;

import tech.dut.fasto.config.security.DomainUserDetailsService;
import tech.dut.fasto.config.security.SecurityUtils;
import tech.dut.fasto.config.security.jwt.TokenProvider;
import tech.dut.fasto.errors.FastoAlertException;
import tech.dut.fasto.util.AppUtil;
import tech.dut.fasto.util.VnPayUtil;
import tech.dut.fasto.util.constants.Constants;
import tech.dut.fasto.web.rest.user.vnpay.dto.request.VNPayCreateTokenPaymentUrlRequestDto;
import tech.dut.fasto.web.rest.user.vnpay.dto.request.VNPayCreateTokenRequestDto;
import tech.dut.fasto.web.rest.user.vnpay.dto.request.VNPayCreateUrlRequestDto;
import tech.dut.fasto.web.rest.user.vnpay.dto.response.UserCardResponseDto;
import tech.dut.fasto.web.rest.user.vnpay.dto.response.VNPayCreateUrlResponseDto;
import tech.dut.fasto.web.rest.user.vnpay.dto.response.VNPayReturnUrlResponseDto;
import tech.dut.fasto.web.rest.user.vnpay.dto.response.VNPayTokenResponseDto;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
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
public class VnPayUserServiceImpl implements VnPayUserService {


    private final BillRepository billRepository;

    private final BillItemRepository billItemRepository;

    private final PaymentRepository paymentRepository;

    private final UserRepository userRepository;

    private final FastoProperties fastoProperties;

    private final MessageService messageService;

    private final VNPayTokenInfoRepository vnPayTokenInfoRepository;

    private final TokenProvider tokenProvider;

    private final NotificationService notificationService;

    private final DeviceTokenInfoRepository deviceTokenInfoRepository;

    private final ShopRepository shopRepository;

    private final EmailService emailService;

    private final Logger logger = LoggerFactory.getLogger(VnPayUserServiceImpl.class);

    @Override
    @Transactional(rollbackFor = FastoAlertException.class)
    public VNPayCreateUrlResponseDto createPaymentUrl(VNPayCreateUrlRequestDto requestDTO, HttpServletRequest request) {
        Bill bill = billRepository.findByIdAndStatus(Long.valueOf(requestDTO.getBillId()), BillStatus.CREATED).orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.payment.create.failed"), messageService.getMessage("error.bill.not.found")));
        String ipAdd = VnPayUtil.getIpAddress(request);

        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.VNP_DATE_FORMAT);

        String orderInfo = "Thanh toan cho don hang " + requestDTO.getBillId();
        String vnpCreateDate = formatter.format(zonedDateTime);

        String vnpTxnRef = requestDTO.getBillId() + "_" + vnpCreateDate + "_ORDER";

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
        VNPayCreateUrlResponseDto responseDTO = new VNPayCreateUrlResponseDto();
        responseDTO.setUrl(paymentUrl);
        responseDTO.setBillId(Long.parseLong(requestDTO.getBillId()));
        bill.setStatus(BillStatus.PENDING);
        bill.setExpiredTime(Instant.now().plus(15,ChronoUnit.MINUTES));
        bill.setUrlPayment(paymentUrl);
        return responseDTO;
    }

    @Override
    public VNPayReturnUrlResponseDto getReturnPaymentUrl(HttpServletRequest request) throws UnsupportedEncodingException {
        Map fields = new HashMap();
        for (Enumeration<String> params = request.getParameterNames(); params.hasMoreElements(); ) {
            String fieldName = URLDecoder.decode(params.nextElement(), StandardCharsets.UTF_8.toString());
            String fieldValue = URLDecoder.decode(request.getParameter(fieldName), StandardCharsets.UTF_8.toString());
            if (fieldName.equals("vnp_OrderInfo")) {
                fieldValue = fieldValue.replace(" ", "+");
            }
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                fields.put(fieldName, fieldValue);
            }
        }
        String vnpSecureHash = request.getParameter("vnp_SecureHash");
        if (fields.containsKey("vnp_SecureHashType")) {
            fields.remove("vnp_SecureHashType");
        }
        if (fields.containsKey("vnp_SecureHash")) {
            fields.remove("vnp_SecureHash");
        }
        String signValue = VnPayUtil.hashAllFields(fastoProperties.getVnPay().getHashSecret(), fields);
        VNPayReturnUrlResponseDto vnPayReturnUrlResponseDTO = new VNPayReturnUrlResponseDto();
        if (signValue.equals(vnpSecureHash)) {
            if (VNPayEnum.PaymentStatus.SUCCESS.getCode().equals(request.getParameter("vnp_ResponseCode"))) {
                vnPayReturnUrlResponseDTO.setResponseCode(VNPayEnum.PaymentStatus.SUCCESS.getCode());
                vnPayReturnUrlResponseDTO.setMessage(VNPayEnum.PaymentStatus.SUCCESS.getMessage());
                vnPayReturnUrlResponseDTO.setSignData(signValue);
            }
        } else {
            vnPayReturnUrlResponseDTO.setResponseCode(VNPayEnum.PaymentStatus.INVALID_CHECKSUM.getCode());
            vnPayReturnUrlResponseDTO.setMessage(VNPayEnum.PaymentStatus.INVALID_CHECKSUM.getMessage());
            vnPayReturnUrlResponseDTO.setSignData(signValue);
        }
        return vnPayReturnUrlResponseDTO;
    }

    @Override
    @Transactional(rollbackFor = {FastoAlertException.class})
    public VNPayReturnUrlResponseDto getIPNUrl(HttpServletRequest request) throws UnsupportedEncodingException {


        Map fields = new HashMap();
        for (Enumeration<String> params = request.getParameterNames(); params.hasMoreElements(); ) {
            String fieldName = URLEncoder.encode(params.nextElement(), StandardCharsets.US_ASCII);
            String fieldValue = URLEncoder.encode(request.getParameter(fieldName), StandardCharsets.US_ASCII);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                if (fieldName.equals("vnp_txn_desc")) {
                    fields.put(fieldName, VnPayUtil.getVnpOrderInfoForApp(fieldValue));
                } else {
                    fields.put(fieldName, fieldValue);
                }
            }
        }
        if (fields.containsKey("vnp_secure_hash_type")) {
            fields.remove("vnp_secure_hash_type");
        }
        if (fields.containsKey("vnp_secure_hash")) {
            fields.remove("vnp_secure_hash");
        }


        VNPayReturnUrlResponseDto responseDTO = new VNPayReturnUrlResponseDto();
        String VNPayService = request.getParameter("vnp_command");
        if(null ==VNPayService ){
            VNPayService = "";
        }
        switch (VNPayService) {
            case Constants.TOKEN_CREATE: {

                responseDTO = getTokenUrl(request, fields);
                break;
            }

            case Constants.TOKEN_PAY: {

                    responseDTO = handleValidatePayment(fields, request);
                break;
            }
            case Constants.PAY_AND_CREATE: {

                if ( VNPayEnum.TokenStatus.SUCCESS.getCode().equals(getTokenUrl(request, fields).getResponseCode()) && VNPayEnum.TokenStatus.SUCCESS.getCode().equals(handleValidatePayment(fields, request).getResponseCode())) {
                    responseDTO.setResponseCode(VNPayEnum.TokenStatus.SUCCESS.getCode());
                }
                break;
            }
            default: {

                responseDTO = handleValidatePayment(fields, request);
            }
        }
        if (null == responseDTO.getResponseCode()) {
            responseDTO.setResponseCode(VNPayEnum.TokenStatus.TRANSACTION_FAILED.getCode());
        }
        return responseDTO;
    }

    @Override
    @Transactional(rollbackFor = {FastoAlertException.class, UnsupportedEncodingException.class})
    public VNPayReturnUrlResponseDto handleValidatePayment(Map fields, HttpServletRequest request) {

        VNPayReturnUrlResponseDto responseDTO = new VNPayReturnUrlResponseDto();
        String vnpSecureHash = request.getParameter("vnp_SecureHash");
        if (null == vnpSecureHash) {
            vnpSecureHash = request.getParameter("vnp_secure_hash");
        }



        String signValue = VnPayUtil.hashAllFields(fastoProperties.getVnPay().getHashSecret(), fields);

        try {
//            if (signValue.equals(vnpSecureHash)) {
                //Verify signature OK

                Long billId= null;
                Long shopId =null;
                if(null == fields.get("vnp_TxnRef")){
                    billId = Long.valueOf(fields.get("vnp_txn_ref").toString().split("_")[1]);

                }else{
                    String [] listVnp = fields.get("vnp_TxnRef").toString().split("_");
                    if(!listVnp[0].equals("deposit")){
                        billId = Long.valueOf(fields.get("vnp_TxnRef").toString().split("_")[0]);
                    }else{
                        shopId = Long.valueOf(fields.get("vnp_TxnRef").toString().split("_")[1]);
                        billId = Long.valueOf(fields.get("vnp_TxnRef").toString().split("_")[3]);
                    }

                }
                String amount;
                if(null != fields.get("vnp_Amount")){
                    amount= fields.get("vnp_Amount").toString();
                }
                else {
                    amount= fields.get("vnp_amount").toString();
                }
                //Verify order is exist
                boolean checkOrderId = billRepository.existsById(billId);
                //Verify amount
                boolean checkAmount = billRepository.existsByTotalPaymentAndId(BigDecimal.valueOf(Long.parseLong(amount) / 100), billId);
                //Verify order status in database
                boolean checkOrderStatus = billRepository.existsByIdAndStatus(billId, BillStatus.PENDING);
                if (checkOrderId) {

                    if (checkAmount) {

                        if (checkOrderStatus) {

                            String responseCode;
                            if(null != request.getParameter("vnp_ResponseCode")){
                                responseCode= request.getParameter("vnp_ResponseCode");
                            }
                            else {
                                responseCode= request.getParameter("vnp_response_code");
                            }
                            if (VNPayEnum.PaymentStatus.SUCCESS.getCode().equals(responseCode)) {

                                this.handleSuccessPayment(billId, shopId, request);

                                responseDTO.setResponseCode(responseCode);
                                responseDTO.setMessage("VNPAYMENT_SUCCESS");
                            } else {

                                this.handleFailPayment(billId,shopId, request);
                                responseDTO.setResponseCode(responseCode);
                                responseDTO.setMessage("VNPAYMENT_FAILED");
                            }
                        } else {
                            responseDTO.setResponseCode(VNPayEnum.PaymentStatus.ALREADY_CONFIRM.getCode());
                            responseDTO.setMessage(VNPayEnum.PaymentStatus.ALREADY_CONFIRM.getMessage());
                        }
                    } else {
                        responseDTO.setResponseCode(VNPayEnum.PaymentStatus.INVALID_AMOUNT.getCode());
                        responseDTO.setMessage(VNPayEnum.PaymentStatus.INVALID_AMOUNT.getMessage());
                    }
                } else {
                    responseDTO.setResponseCode(VNPayEnum.PaymentStatus.ORDER_NOT_FOUND.getCode());
                    responseDTO.setMessage(VNPayEnum.PaymentStatus.ORDER_NOT_FOUND.getMessage());
                }
//            }
//            else {
//                responseDTO.setResponseCode(VNPayEnum.PaymentStatus.INVALID_CHECKSUM.getCode());
//                responseDTO.setMessage(VNPayEnum.PaymentStatus.INVALID_CHECKSUM.getMessage());
//            }
        } catch (Exception e) {
            responseDTO.setResponseCode(VNPayEnum.PaymentStatus.UNKNOW_ERR.getCode());
            responseDTO.setMessage(VNPayEnum.PaymentStatus.UNKNOW_ERR.getMessage());
        }
        return responseDTO;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserCardResponseDto> getAllListCards() {
        String email = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.bill.create.failed"), messageService.getMessage("error.authenticate.unauthorized.user")));
        User user = userRepository.findByEmailAndStatus(email, UserStatus.ACTIVATED).orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.bill.create.failed"), messageService.getMessage("error.authenticate.user.already.activated")));
        return vnPayTokenInfoRepository.getAllCardNumbers(user.getId());
    }

    @Override
    @Transactional(rollbackFor = FastoAlertException.class)
    public void handleSuccessPayment(Long billId, Long shopId, HttpServletRequest request) {

        Bill bill = billRepository.findById(billId).orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.payment.do.not.use.pin"), messageService.getMessage("error.bill.not.found")));

        logger.info("khoa1");

        String bankCode;
        if(null != request.getParameter("vnp_BankCode")){
            bankCode = request.getParameter("vnp_BankCode");
        }else{
            bankCode = request.getParameter("vnp_bank_code");
        }
        Payment payment = new Payment();
        payment.setName(VnPayUtil.getTypePayment(bankCode));
        payment.setPaymentInfo(request.getQueryString());
        payment.setStatus(true);
        payment.setBill(bill);
        logger.info("khoa2");
//        logger.info(shopId.toString());
        if(shopId !=null){
            Shop shop = shopRepository.findById(shopId).orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.bill.create.failed"), messageService.getMessage("error.authenticate.unauthorized.user")));
            shop.setIsDeposit(Boolean.TRUE);
            payment.setType(PaymentType.DEPOSIT);
            bill.setStatus(BillStatus.DEPOSITED);
            this.emailService.sendEmailForActiveBusinessShop(shop.getUser().getEmail(), shop.getName());
            logger.info("khoa3");
        }else{
            logger.info("khoa77");
            payment.setType(PaymentType.NORMAL);
            bill.setStatus(BillStatus.PAID);
            List<BillItem> billItems = billItemRepository.findAllByBillId(billId);
            for (BillItem item : billItems) {
                Long tmpCount = item.getProduct().getCountPay() + item.getAmount();
                item.getProduct().setCountPay(tmpCount);
            }
            String title = "Fasto";
            String body = "Đơn hàng %d đã thanh toán thành công";
            NotificationDto notificationDto = new NotificationDto();
            notificationDto.setTitle(title);
            notificationDto.setBody(String.format(String.format(body, billId)));
            handleSendNotification(notificationDto);
            logger.info("khoa4");
        }
        paymentRepository.save(payment);
        logger.info("khoa5");
    }

    @Transactional
    public void handleSendNotification(NotificationDto notificationDto) {
        logger.info("khoa6");
//        String email = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.bill.create.failed"), messageService.getMessage("error.authenticate.unauthorized.user")));
//        User user = userRepository.findByEmailAndStatus(email, UserStatus.ACTIVATED).orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.bill.create.failed"), messageService.getMessage("error.authenticate.user.already.activated")));
//        List<String> tokens = deviceTokenInfoRepository.getAllByUserId(user.getId());
//        notificationService.sendMultipleNotification(tokens, notificationDto);

    }

    @Override
    @Transactional(rollbackFor = FastoAlertException.class)
    public void handleFailPayment(Long billId,Long shopId, HttpServletRequest request) {
        Bill bill = billRepository.findById(billId).orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.payment.do.not.use.pin"), messageService.getMessage("error.bill.not.found")));
        bill.setStatus(BillStatus.FAILED);
        String bankCode;
        if(null != request.getParameter("vnp_BankCode")){
            bankCode = request.getParameter("vnp_BankCode");
        }else{
            bankCode = request.getParameter("vnp_bank_code");
        }
        if(shopId !=null){

        }
        Payment payment = new Payment();
        payment.setName(VnPayUtil.getTypePayment(bankCode));
        bill.setPayments(Sets.newHashSet(payment));
        payment.setPaymentInfo(request.getQueryString());
        payment.setStatus(false);
        paymentRepository.save(payment);
        String title = "Fasto";
        String body = "Đơn hàng %d đã thanh toán không thành công";
        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setTitle(title);
        notificationDto.setBody(String.format(String.format(body, billId)));
        handleSendNotification(notificationDto);
    }

    @Override
    public VNPayTokenResponseDto createToken(HttpServletRequest request, VNPayCreateTokenRequestDto vnPayCreateTokenRequestDto) {
        VNPayTokenResponseDto vnPayCreateUrlResponseDTO = new VNPayTokenResponseDto();
        String ipAdd = AppUtil.convertDoublePointToPoint(VnPayUtil.getIpAddress(request));

        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of(Constants.VNP_TIMEZONE));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.VNP_DATE_FORMAT);

        String vnpCreateDate = formatter.format(zonedDateTime);

        String email = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.payment.create.token.failed"), messageService.getMessage("error.authenticate.unauthorized.user")));
        User user = userRepository.findByEmailAndStatus(email, UserStatus.ACTIVATED).orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.payment.create.token.failed"), messageService.getMessage("error.authenticate.user.already.activated")));

        Long userId = user.getId();
        String txnInfo = "CREATE TOKEN FOR USER " + userId;
        String vnpTxnRef = userId + Constants.UNDER_LINE + vnpCreateDate + "_CREATE_TOKEN";

        Map<String, String> vnpParams = new HashMap<>();
        vnpParams.put(Constants.VNP_VERSION_KEY, fastoProperties.getVnPay().getVersion());
        vnpParams.put("vnp_command", "token_create");
        vnpParams.put("vnp_tmn_code", fastoProperties.getVnPay().getTmnCode());
        vnpParams.put("vnp_app_user_id", userId.toString());
        vnpParams.put("vnp_bank_code", vnPayCreateTokenRequestDto.getBankCode());
        vnpParams.put("vnp_locale", vnPayCreateTokenRequestDto.getVnpLocale());
        if (vnPayCreateTokenRequestDto.getBankCode().equals(BankType.VISA.toString()) || vnPayCreateTokenRequestDto.getBankCode().equals("MASTERCARD")) {
            vnpParams.put("vnp_card_type", "02");
        } else {
            vnpParams.put("vnp_card_type", "01");
        }
        vnpParams.put("vnp_txn_ref", vnpTxnRef);
        vnpParams.put("vnp_txn_desc", txnInfo);
        vnpParams.put("vnp_return_url", vnPayCreateTokenRequestDto.getVnpReturnUrl());
        vnpParams.put("vnp_cancel_url", vnPayCreateTokenRequestDto.getVnpReturnUrl());
        vnpParams.put("vnp_ip_addr", ipAdd);
        vnpParams.put("vnp_create_date", vnpCreateDate);

        List<String> fieldNames = new ArrayList<>(vnpParams.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
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
        queryUrl += "&vnp_secure_hash_type=HMACSHA512&vnp_secure_hash=" + vnpSecureHash;
        String tokenUrl = "https://sandbox.vnpayment.vn/token_ui/create-token.html" + "?" + queryUrl;
        vnPayCreateUrlResponseDTO.setUrl(tokenUrl);
        return vnPayCreateUrlResponseDTO;
    }

    @Override
    @Transactional(rollbackFor = FastoAlertException.class)
    public VNPayCreateUrlResponseDto createTokenPay(VNPayCreateUrlRequestDto requestDTO, HttpServletRequest request) {
        Long billId = Long.valueOf(requestDTO.getBillId());
        String email = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.payment.create.token.and.pay.failed"), messageService.getMessage("error.authenticate.unauthorized.user")));
        User user = userRepository.findByEmailAndStatus(email, UserStatus.ACTIVATED).orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.payment.create.token.and.pay.failed"), messageService.getMessage("error.authenticate.user.already.activated")));
        Long userId = user.getId();
        Optional<Bill> optionalBill = billRepository.findByIdAndStatusAndUserId(billId, BillStatus.CREATED, userId);
        if (optionalBill.isPresent()) {
            Bill bill = optionalBill.get();

            BigDecimal amount = bill.getTotalPayment();
            String ipAdd = AppUtil.convertDoublePointToPoint(VnPayUtil.getIpAddress(request));
            ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of(Constants.VNP_TIMEZONE));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.VNP_DATE_FORMAT);

            String orderInfo = "Thanh toan cho don hang " + requestDTO.getBillId();
            String vnpCreateDate = formatter.format(zonedDateTime);

            String vnpTxnRef = "ORDER" + Constants.UNDER_LINE + requestDTO.getBillId() + Constants.UNDER_LINE + vnpCreateDate;
            Map<String, String> vnpParams = new HashMap<>();
            vnpParams.put(Constants.VNP_VERSION_KEY, fastoProperties.getVnPay().getVersion());
            vnpParams.put("vnp_command", "pay_and_create");
            vnpParams.put("vnp_tmn_code", fastoProperties.getVnPay().getTmnCode());
            vnpParams.put("vnp_app_user_id", userId.toString());
            vnpParams.put("vnp_bank_code", requestDTO.getBankCode());
            vnpParams.put("vnp_locale", requestDTO.getVnpLocale());
            if (requestDTO.getBankCode().equals(BankType.VISA.toString()) || requestDTO.getBankCode().equals("MASTERCARD")) {
                vnpParams.put("vnp_card_type", "02");
            } else {
                vnpParams.put("vnp_card_type", "01");
            }
            vnpParams.put("vnp_txn_ref", vnpTxnRef);
            vnpParams.put("vnp_amount", String.valueOf(amount.multiply(BigDecimal.valueOf(100l))));
            vnpParams.put("vnp_curr_code", "VND");
            vnpParams.put("vnp_txn_desc", orderInfo);
            vnpParams.put("vnp_return_url", requestDTO.getVnpReturnUrl());
            vnpParams.put("vnp_cancel_url", requestDTO.getVnpReturnUrl());
            vnpParams.put("vnp_ip_addr", ipAdd);
            vnpParams.put("vnp_create_date", vnpCreateDate);
            vnpParams.put("vnp_secure_hash_type", "HMACSHA512");
            vnpParams.put("vnp_store_token", "1");

            List<String> fieldNames = new ArrayList<>(vnpParams.keySet());
            Collections.sort(fieldNames);
            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();
            Iterator<String> itr = fieldNames.iterator();
            while (itr.hasNext()) {
                String fieldName = itr.next();
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
            queryUrl += "&vnp_secure_hash=" + vnpSecureHash;
            String tokenPayUrl = "https://sandbox.vnpayment.vn/token_ui/pay-create-token.html" + "?" + queryUrl;
            VNPayCreateUrlResponseDto responseDTO = new VNPayCreateUrlResponseDto();
            responseDTO.setUrl(tokenPayUrl);
            responseDTO.setBillId(Long.parseLong(requestDTO.getBillId()));
            bill.setStatus(BillStatus.PENDING);
            return responseDTO;
        } else {
            throw new FastoAlertException(messageService.getMessage("error.code.payment.create.token.and.pay.failed"), messageService.getMessage("error.bill.not.found"));
        }

    }

    @Override
    @Transactional(rollbackFor = FastoAlertException.class)
    public VNPayReturnUrlResponseDto getTokenUrl(HttpServletRequest request, Map fields) {
        VNPayReturnUrlResponseDto responseDTO = new VNPayReturnUrlResponseDto();
        String signValue = VnPayUtil.hashAllFields(fastoProperties.getVnPay().getHashSecret(), fields);
        String vnpSecureHash = request.getParameter("vnp_secure_hash");
        try {
            Long id = Long.parseLong(request.getParameter("vnp_app_user_id"));
            Optional<User> optionalUser = userRepository.findByIdAndStatus(id, UserStatus.ACTIVATED);
            if (signValue.equals(vnpSecureHash) && optionalUser.isPresent()) {
                User user = optionalUser.get();
                Long userId = Long.valueOf(fields.get("vnp_app_user_id").toString());
                if (!userId.equals(user.getId())) {
                    responseDTO.setResponseCode(request.getParameter("vnp_response_code"));
                    responseDTO.setMessage("VNPAYMENT_FAILED");
                } else {
                    if (VNPayEnum.TokenStatus.SUCCESS.getCode().equals(request.getParameter("vnp_response_code"))) {
                        handleGetTokenSuccess(userId, fields);

                        responseDTO.setResponseCode(request.getParameter("vnp_response_code"));
                        responseDTO.setMessage(messageService.getMessage("vnpay_create_token_successfully"));
                    } else {
                        responseDTO.setResponseCode(request.getParameter("vnp_response_code"));
                        responseDTO.setMessage(messageService.getMessage("vnpay_create_token_failed"));
                    }
                }
            } else {
                responseDTO.setResponseCode(VNPayEnum.PaymentStatus.INVALID_CHECKSUM.getCode());
                responseDTO.setMessage(messageService.getMessage("vnpay_payment_invalid_checksum"));
            }

        } catch (Exception e) {
            responseDTO.setResponseCode(VNPayEnum.PaymentStatus.UNKNOW_ERR.getCode());
            responseDTO.setMessage(messageService.getMessage("error.unknown.msg"));
        }
        return responseDTO;
    }

    @Override
    @Transactional(rollbackFor = FastoAlertException.class)
    public VNPayCreateUrlResponseDto createTokenPayment(HttpServletRequest request, VNPayCreateTokenPaymentUrlRequestDto requestDTO) {
        String email = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.payment.create.payment.by.token.failed"), messageService.getMessage("error.authenticate.unauthorized.user")));
        User user = userRepository.findByEmailAndStatus(email, UserStatus.ACTIVATED).orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.payment.create.payment.by.token.failed"), messageService.getMessage("error.authenticate.user.already.activated")));

        VNPayTokenInfo vnPayTokenInfo = vnPayTokenInfoRepository.findByUserIdAndCardNumber(user.getId(), requestDTO.getCardNumber()).orElseThrow(
                () -> new FastoAlertException(messageService.getMessage("error.code.payment.create.payment.by.token.failed"), messageService.getMessage("error.code.payment.create.token.failed"))
        );

        Long billId = Long.valueOf(requestDTO.getBillId());
        Bill bill = billRepository.findByIdAndStatus(billId, BillStatus.CREATED).orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.payment.create.payment.by.token.failed"), messageService.getMessage("error.bill.not.found")));

            String ipAdd = AppUtil.convertDoublePointToPoint(VnPayUtil.getIpAddress(request));

            ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of(Constants.VNP_TIMEZONE));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.VNP_DATE_FORMAT);

            String orderInfo = "Thanh toan cho don hang " + requestDTO.getBillId();
            String vnpCreateDate = formatter.format(zonedDateTime);

            String vnpTxnRef = "ORDER" + Constants.UNDER_LINE + requestDTO.getBillId() + Constants.UNDER_LINE + vnpCreateDate;

            Map<String, String> vnpParams = new HashMap<>();
            vnpParams.put(Constants.VNP_VERSION_KEY, fastoProperties.getVnPay().getVersion());
            vnpParams.put("vnp_tmn_code", fastoProperties.getVnPay().getTmnCode());
            vnpParams.put("vnp_command", "token_pay");
            vnpParams.put("vnp_txn_ref", vnpTxnRef);
            vnpParams.put("vnp_app_user_id", user.getId().toString());
            vnpParams.put(Constants.VNP_TOKEN_KEY, tokenProvider.getVNPayTokenJwtToken(vnPayTokenInfo.getToken()));
            vnpParams.put("vnp_amount", String.valueOf(bill.getTotalPayment().multiply(BigDecimal.valueOf(100l))));
            vnpParams.put("vnp_curr_code", "VND");
            vnpParams.put("vnp_txn_desc", orderInfo);
            vnpParams.put("vnp_create_date", vnpCreateDate);
            vnpParams.put("vnp_ip_addr", ipAdd);
            vnpParams.put("vnp_locale", requestDTO.getVnpLocale());
            vnpParams.put("vnp_return_url", requestDTO.getVnpReturnUrl());
            vnpParams.put("vnp_cancel_url", requestDTO.getVnpReturnUrl());

            List<String> fieldNames = new ArrayList<>(vnpParams.keySet());
            Collections.sort(fieldNames);
            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();
            Iterator<String> itr = fieldNames.iterator();
            while (itr.hasNext()) {
                String fieldName = itr.next();
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
            queryUrl += "&vnp_secure_hash_type=HMACSHA512&vnp_secure_hash=" + vnpSecureHash;
            String paymentUrl = "https://sandbox.vnpayment.vn/token_ui/payment-token.html" + "?" + queryUrl;

            VNPayCreateUrlResponseDto responseDTO = new VNPayCreateUrlResponseDto();
            responseDTO.setUrl(paymentUrl);
            responseDTO.setBillId(Long.parseLong(requestDTO.getBillId()));
            bill.setStatus(BillStatus.PENDING);
            bill.setExpiredTime(Instant.now().plus(15,ChronoUnit.MINUTES));
            bill.setUrlPayment(paymentUrl);
            return responseDTO;
    }

    @Override
    @Transactional(rollbackFor = FastoAlertException.class)
    public VNPayTokenResponseDto deleteToken(HttpServletRequest request, Long id) {
        String email = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.payment.delete.token.failed"), messageService.getMessage("error.authenticate.unauthorized.user")));
        User user = userRepository.findByEmailAndStatus(email, UserStatus.ACTIVATED).orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.payment.delete.token.failed"), messageService.getMessage("error.authenticate.user.already.activated")));

        VNPayTokenInfo vnPayTokenInfo = vnPayTokenInfoRepository.findByIdAndUserId(id, user.getId()).orElseThrow(
                () -> new FastoAlertException(messageService.getMessage("error.code.payment.delete.token.failed"), messageService.getMessage("error.code.payment.create.token.failed"))
        );

        String ipAdd = AppUtil.convertDoublePointToPoint(VnPayUtil.getIpAddress(request));

        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of(Constants.VNP_TIMEZONE));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.VNP_DATE_FORMAT);

        String vnpCreateDate = formatter.format(zonedDateTime);

        String txnInfo = "DELETE TOKEN FOR USER " + user.getId();
        String vnpTxnRef = user.getId() + Constants.UNDER_LINE + vnpCreateDate + "_DELETE_TOKEN";

        Map<String, String> vnpParams = new HashMap<>();
        vnpParams.put(Constants.VNP_VERSION_KEY, fastoProperties.getVnPay().getVersion());
        vnpParams.put("vnp_tmn_code", fastoProperties.getVnPay().getTmnCode());
        vnpParams.put("vnp_command", "token_remove");
        vnpParams.put("vnp_app_user_id", user.getId().toString());
        vnpParams.put("vnp_txn_ref", vnpTxnRef);
        vnpParams.put(Constants.VNP_TOKEN_KEY, tokenProvider.getVNPayTokenJwtToken(vnPayTokenInfo.getToken()));
        vnpParams.put("vnp_txn_desc", txnInfo);
        vnpParams.put("vnp_ip_addr", ipAdd);
        vnpParams.put("vnp_create_date", vnpCreateDate);

        List<String> fieldNames = new ArrayList<>(vnpParams.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
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
        VNPayTokenResponseDto vnPayCreateUrlResponseDTO = new VNPayTokenResponseDto();
        String queryUrl = query.toString();
        String vnpSecureHash = VnPayUtil.hmacSHA512(fastoProperties.getVnPay().getHashSecret(), hashData.toString());
        queryUrl += "&vnp_secure_hash_type=HMACSHA512&vnp_secure_hash=" + vnpSecureHash;
        String tokenUrl = "https://sandbox.vnpayment.vn/token_ui/remove-token.html" + "?" + queryUrl;
        vnPayCreateUrlResponseDTO.setUrl(tokenUrl);

        vnPayTokenInfo.setDeleteFlag(true);
        List<VNPayTokenInfo> vnPayTokenInfos = vnPayTokenInfoRepository.findAllByUserIdAndIdNot(user.getId(), id);
        if (null != vnPayTokenInfos && !vnPayTokenInfos.isEmpty()) {
            vnPayTokenInfos.get(0).setIsDefault(true);
        }
        return vnPayCreateUrlResponseDTO;
    }

    @Override
    @Transactional(rollbackFor = FastoAlertException.class)
    public void handleGetTokenSuccess(Long userId, Map fields) {
        User user = userRepository.findByIdAndStatus(userId, UserStatus.ACTIVATED).orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.payment.handle.status.success.token.failed"), messageService.getMessage("error.authenticate.user.already.activated")));
        String bankCode = fields.get("vnp_bank_code").toString();
        String cardNumber = fields.get("vnp_card_number").toString();
        String token = fields.get(Constants.VNP_TOKEN_KEY).toString();
        Optional<VNPayTokenInfo> oVNPayTokenInfoOptional = vnPayTokenInfoRepository.findByUserIdAndBankCodeAndCardNumber(userId, bankCode, cardNumber);

        VNPayTokenInfo vnPayTokenInfo;
        if (oVNPayTokenInfoOptional.isPresent()) {
            vnPayTokenInfo = oVNPayTokenInfoOptional.get();
        } else {
            vnPayTokenInfo = new VNPayTokenInfo();
            vnPayTokenInfo.setUser(user);
            vnPayTokenInfo.setBankCode(bankCode);
            vnPayTokenInfo.setCardNumber(cardNumber);
        }
        vnPayTokenInfo.setToken(tokenProvider.createVNPayToken(token));
        vnPayTokenInfo.setDeleteFlag(false);

        List<Long> vnPayTokenIds = vnPayTokenInfoRepository.userGetListVNPayTokenInfoIdByUserId(userId);
        if (null == vnPayTokenIds || vnPayTokenIds.isEmpty()) {
            vnPayTokenInfo.setIsDefault(true);
        }
        vnPayTokenInfoRepository.save(vnPayTokenInfo);
    }

}
