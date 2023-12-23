package tech.dut.fasto.common.service;

import com.google.firebase.messaging.WebpushConfig;
import tech.dut.fasto.common.dto.NotificationDto;

import java.util.List;

public interface NotificationService {

    void sendNotification(String token,NotificationDto notificationDto);

    String handleToken(String token);

    void sendMultipleNotification(List<String> tokens,  NotificationDto notificationDto);

    WebpushConfig handCreateWebNotification(NotificationDto notificationDto);
}
