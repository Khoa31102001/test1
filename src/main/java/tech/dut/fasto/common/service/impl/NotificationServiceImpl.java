package tech.dut.fasto.common.service.impl;

import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.dut.fasto.common.domain.DeviceTokenInfo;
import tech.dut.fasto.common.domain.User;
import tech.dut.fasto.common.domain.enumeration.UserStatus;
import tech.dut.fasto.common.dto.NotificationDto;
import tech.dut.fasto.common.repository.DeviceTokenInfoRepository;
import tech.dut.fasto.common.repository.UserRepository;
import tech.dut.fasto.common.service.NotificationService;
import tech.dut.fasto.config.security.SecurityUtils;
import tech.dut.fasto.errors.FastoAlertException;

import java.util.List;


@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final DeviceTokenInfoRepository deviceTokenInfoRepository;

    private final FirebaseMessaging fcm;

    private final UserRepository userRepository;

    private final MessageService messageService;

    @Override
    @Transactional(readOnly = true)
    @Async
    public void sendNotification(String token, NotificationDto notificationDto) {
        token = handleToken(token);
        try {
            WebpushConfig webpushConfig = handCreateWebNotification(notificationDto);
            Message message = Message.builder().setToken(token).setWebpushConfig(webpushConfig).build();
            fcm.send(message);

        } catch (Exception e) {
            throw new FastoAlertException(messageService.getMessage("error.code.notification.send.single.failed"), e.getMessage());
        }
    }

    @Override
    public WebpushConfig handCreateWebNotification(NotificationDto notificationDto) {
        WebpushNotification.Builder builder = WebpushNotification.builder()
                .setIcon("https://www.facebook.com/images/fb_icon_325x325.png")
                .setTitle(notificationDto.getTitle())
                .setBody(notificationDto.getBody());
        return WebpushConfig.builder().setNotification(builder.build()).build();
    }

    @Override
    @Transactional(readOnly = true)
    public String handleToken(String token) {
        if (null == token) {
            String email = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.notification.send.multiple.failed"), messageService.getMessage("error.authenticate.unauthorized.user")));
            User user = userRepository.findByEmailAndStatus(email, UserStatus.ACTIVATED).orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.notification.send.multiple.failed"), messageService.getMessage("error.authenticate.user.already.activated")));
            DeviceTokenInfo deviceTokenInfo = deviceTokenInfoRepository.findByDisableAndUserId(false, user.getId()).
                    orElseThrow(() -> new FastoAlertException(messageService.getMessage("error.code.notification.send.multiple.failed"), messageService.getMessage("error.authenticate.user.already.activated")));
            token = deviceTokenInfo.getToken();
        }
        return token;
    }

    @Override
    @Transactional(readOnly = true)
    @Async
    public void sendMultipleNotification(List<String> tokens, NotificationDto notificationDto) {
        try {
            WebpushConfig webpushConfig = handCreateWebNotification(notificationDto);
            MulticastMessage multicastMessage = MulticastMessage.builder().addAllTokens(tokens).setWebpushConfig(webpushConfig).build();
            fcm.sendMulticast(multicastMessage);
        } catch (Exception e) {
            throw new FastoAlertException(messageService.getMessage("error.code.notification.send.multiple.failed"), e.getMessage());
        }
    }
}
