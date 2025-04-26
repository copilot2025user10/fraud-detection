package com.ey.fraud_detection.consumer;

import com.ey.fraud_detection.producer.NotificationProducer;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TransactionForCheckConsumer {

    final NotificationProducer notificationProducer;

    public TransactionForCheckConsumer(NotificationProducer notificationProducer) {
        this.notificationProducer = notificationProducer;
    }

    @KafkaListener(topics = "{transaction.fraud.check.topic}", groupId = "${spring.kafka.group-id}")
    public void listen(String message) {
        System.out.println("Received Message: " + message);
        notificationProducer.sendMessage(message);
    }
}
