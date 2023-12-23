package tech.dut.fasto.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.dut.fasto.common.domain.enumeration.BankType;
import tech.dut.fasto.util.constants.Constants;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class VnPayUtil {


    private VnPayUtil() {
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(VnPayUtil.class);


    public static String hmacSHA512(final String key, final String data) {
        try {

            if (key == null || data == null) {
                LOGGER.error("Key or data null");
                throw new NullPointerException();
            }
            final Mac hmac512 = Mac.getInstance("HmacSHA512");
            byte[] hmacKeyBytes = key.getBytes();
            final SecretKeySpec secretKey = new SecretKeySpec(hmacKeyBytes, "HmacSHA512");
            hmac512.init(secretKey);
            byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
            byte[] result = hmac512.doFinal(dataBytes);
            StringBuilder sb = new StringBuilder(2 * result.length);
            for (byte b : result) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();

        } catch (Exception ex) {
            LOGGER.error("Occur exception in  hmacsha512", ex);
            return "";
        }
    }

    //Util for VNPAY
    public static String hashAllFields(String vnpHashSecret, Map fields) {
        // create a list and sort it
        List fieldNames = new ArrayList(fields.keySet());
        Collections.sort(fieldNames);
        // create a buffer for the md5 input and add the secure secret first
        StringBuilder sb = new StringBuilder();

        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) fields.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                sb.append(fieldName);
                sb.append("=");
                sb.append(fieldValue);
            }
            if (itr.hasNext()) {
                sb.append("&");
            }
        }
        return hmacSHA512(vnpHashSecret, sb.toString());
    }

    public static String getIpAddress(HttpServletRequest request) {
        String ipAdress;
        try {
            ipAdress = request.getHeader("X-FORWARDED-FOR");
            if (ipAdress == null) {
                ipAdress = request.getRemoteAddr();
            }
        } catch (Exception e) {
            ipAdress = "Invalid IP:" + e.getMessage();
        }
        return ipAdress;
    }


    public static String getVnpOrderInfoForApp(String orderInfo) {
        if (!orderInfo.contains("%2B")) {
            return orderInfo;
        }
        return orderInfo.replace("%2B", Constants.PLUS);
    }

    public static BankType getTypePayment(String payment){
        switch (payment) {
            case "VISA":
                return BankType.VISA;
        }
        return BankType.VNPAY;
    }

}
