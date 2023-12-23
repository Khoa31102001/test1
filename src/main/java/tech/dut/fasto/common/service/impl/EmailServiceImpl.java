package tech.dut.fasto.common.service.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import tech.dut.fasto.common.domain.User;
import tech.dut.fasto.common.repository.UserRepository;
import tech.dut.fasto.common.service.EmailService;
import tech.dut.fasto.config.properties.FastoProperties;
import tech.dut.fasto.util.AppUtil;
import tech.dut.fasto.util.constants.Constants;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);

    private final JavaMailSender javaMailSender;

    private final TemplateEngine templateEngine;

    private final UserRepository userRepository;

    private final FastoProperties fastoProperties;

    @Override
    @Async
    public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        log.debug(
                "Send email[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
                isMultipart,
                isHtml,
                to,
                subject,
                content
        );

        MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
            message.setTo(to);
            message.setFrom(fastoProperties.getMail().getFrom());
            message.setSubject(subject);
            message.setText(content, isHtml);
            javaMailSender.send(mimeMessage);
            log.debug("Sent email to User '{}'", to);
        } catch (MailException | MessagingException e) {
            log.warn("Email could not be sent to user '{}'", to, e);
        }
    }

    @Override
    @Async
    public void sendEmailFromTemplate(String to, String subject, String templateName, Map<String, Object> data) {
        Context context = new Context();
        context.setVariables(data);
        String content = this.templateEngine.process(templateName, context);
        this.sendEmail(to, subject, content,false, true);
    }

    @Override
    public String sendEmailForActiveUser(String emailTo) {

        String digitCode = generateDigitCode();

        Map<String,Object> objectMap = new HashMap<>();
        objectMap.put("code", digitCode);
        this.sendEmailFromTemplate(emailTo, Constants.ACTIVE_ACCOUNT_BY_CODE, Constants.TEMPLATE_ACTIVE_CODE, objectMap);
        return digitCode;
    }

    @Override
    public String sendEmailForForgotPassword(String emailTo) {
        String digitCode = generateDigitCode();
        Map<String,Object> objectMap = new HashMap<>();
        objectMap.put("code", digitCode);
        this.sendEmailFromTemplate(emailTo, Constants.FORGOT_PASSWORD_BY_CODE, Constants.TEMPLATE_FORGOT_PASSWORD_CODE, objectMap);
        return digitCode;
    }

    @Override
    @Async
    public void sendEmailForActiveShop(String emailTo, String name) {
        Map<String,Object> objectMap = new HashMap<>();
        objectMap.put("name", name);
        this.sendEmailFromTemplate(emailTo, Constants.ACTIVE_SHOP, Constants.TEMPLATE_ACTIVE_SHOP_CODE, objectMap);
    }

    @Override
    public void sendEmailForActiveBusinessShop(String emailTo, String name) {
        Map<String,Object> objectMap = new HashMap<>();
        objectMap.put("name", name);
        this.sendEmailFromTemplate(emailTo, Constants.ACTIVE_BUSINESS, Constants.ACTIVE_BUSINESS, objectMap);
    }

    private String generateDigitCode() {
        List<String> codes = userRepository.findAll().stream().map(User::getAuthCode).toList();
        String digitCode;
        do {
            digitCode = AppUtil.generateDigitCode();
        } while (codes.contains(digitCode));

        return digitCode;
    }

}
