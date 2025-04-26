package com.ey.fraud_detection.producer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class NotificationProducer {

    @Value(value = "${validation.topic.name:txn-notification}")
    private String notificationTopicName;

    final KafkaTemplate<String, String> kafkaTemplate;

    public NotificationProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String message) {
        kafkaTemplate.send(notificationTopicName, message);
        System.out.println("Sending message: " + message);

    }
}
