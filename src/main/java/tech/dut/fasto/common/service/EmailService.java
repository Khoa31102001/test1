package tech.dut.fasto.common.service;

import java.util.Map;

public interface EmailService {

    void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml);

    public void sendEmailFromTemplate(String to, String subject, String templateName, Map<String,Object> data);

    String sendEmailForActiveUser(String emailTo);

    String sendEmailForForgotPassword(String emailTo);

    void sendEmailForActiveShop(String emailTo, String name);

    void sendEmailForActiveBusinessShop(String emailTo, String name);
}
