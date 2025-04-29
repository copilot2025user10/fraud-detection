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

    @KafkaListener(topics = "transaction-valid-output", groupId = "fraud-detection-group")
    public void listen(String message) {
        System.out.println("Received Message: " + message);

        //Add business logic


        notificationProducer.sendMessage(message);
    }
}
