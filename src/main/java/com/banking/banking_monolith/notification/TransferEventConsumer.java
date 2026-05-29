package com.banking.banking_monolith.notification;

import com.banking.banking_monolith.event.TransferCompletedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class TransferEventConsumer {
    private final NotificationService notificationService;

    public TransferEventConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @KafkaListener(topics="transaction-events", groupId = "banking-monolith")
    public void handleTransferCompleted(TransferCompletedEvent event){
        notificationService.sendNotification(event.sender(), NotificationType.TRANSFER_SENT,"Transfered " + event.amount()
        +event.currency() + " to " + event.receiverName());

        notificationService.sendNotification(event.receiver(), NotificationType.TRANSFER_RECEIVED, event.senderName() + " transfered you " + event.amount() + event.currency());
    }
}
