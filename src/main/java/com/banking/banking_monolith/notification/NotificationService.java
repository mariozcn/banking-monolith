package com.banking.banking_monolith.notification;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);
    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public Notification sendNotification(String receiver, NotificationType type, String message){

        Notification notification = new Notification();
        notification.setNotificationType(type);
        notification.setReceiver(receiver);
        notification.setMessage(message);

        log.info("Sending {} notification to {}: {}", type, receiver, message);
        notification.setNotificationStatus(NotificationStatus.SENT);
        return notificationRepository.save(notification);
    }
}
