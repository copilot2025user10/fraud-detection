package com.ey.fraud_detection.consumer;

import com.ey.fraud_detection.entity.RuleEngineEntity;
import com.ey.fraud_detection.model.Transaction;
import com.ey.fraud_detection.producer.NotificationProducer;
import com.ey.fraud_detection.repository.RuleEngineRepository;
import com.ey.fraud_detection.service.CachingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
public class TransactionForCheckConsumer {

//    @Autowired
//    private RedisTemplate<String, RuleEngineEntity> redisTemplate;
//
//    @Autowired
//    private CachingService cacheService;
//
//    @Autowired
//    private RuleEngineRepository ruleEngineRepository;

    private final RedisTemplate<String, RuleEngineEntity> redisTemplate;
    private final CachingService cacheService;
    private final RuleEngineRepository ruleEngineRepository;
    private final NotificationProducer notificationProducer;

    public TransactionForCheckConsumer(
            RedisTemplate<String, RuleEngineEntity> redisTemplate,
            CachingService cacheService,
            RuleEngineRepository ruleEngineRepository,
            NotificationProducer notificationProducer
    ) {
        this.redisTemplate = redisTemplate;
        this.cacheService = cacheService;
        this.ruleEngineRepository = ruleEngineRepository;
        this.notificationProducer = notificationProducer;
    }

//    final NotificationProducer notificationProducer;

//    public TransactionForCheckConsumer(NotificationProducer notificationProducer) {
//        this.notificationProducer = notificationProducer;
//    }

    @KafkaListener(topics = "transaction-valid-output", groupId = "fraud-detection-group")
    public void listen(List<String> message) {
        System.out.println("Received Message: " + message);

        List<String> list1 = new ArrayList<>();
        list1.add("{\"transactionId\":\"TXN12345\",\"userId\":\"USER001\",\"amount\":100.5,\"currency\":\"USD\",\"timestamp\":1696155330000,\"merchantId\":\"MERCHANT001\",\"location\":\"New York\",\"userRiskScore\":4.8285239146807415,\"merchantRiskScore\":8.845839367221298,\"validationStatus\":\"VALID\"}");
        list1.add("{\"transactionId\":\"TXN12346\",\"userId\":\"USER002\",\"amount\":250.0,\"currency\":\"EUR\",\"timestamp\":1696249245000,\"merchantId\":\"MERCHANT002\",\"location\":\"London\",\"userRiskScore\":3.7136342842774352,\"merchantRiskScore\":8.183467433363266,\"validationStatus\":\"VALID\"}");
        list1.add("{\"transactionId\":\"TXN12347\",\"userId\":\"USER003\",\"amount\":75.25,\"currency\":\"INR\",\"timestamp\":1696343400000,\"merchantId\":\"MERCHANT003\",\"location\":\"Mumbai\",\"userRiskScore\":4.1058280017170725,\"merchantRiskScore\":9.591102108998971,\"validationStatus\":\"VALID\"}");
        list1.add("{\"transactionId\":\"TXN12349\",\"userId\":\"USER005\",\"amount\":50.0,\"currency\":\"AUD\",\"timestamp\":1696528800000,\"merchantId\":\"MERCHANT005\",\"location\":\"Sydney\",\"userRiskScore\":6.899749972551875,\"merchantRiskScore\":8.600171897848295,\"validationStatus\":\"VALID\"}");

        //Add business logic
        //List<Transaction> txns = message.stream().map(m -> {
        List<Transaction> txns = list1.stream().map(m -> {
            try {
                return new ObjectMapper().readValue(m, Transaction.class);
            } catch (Exception e) {
                log.error("Unable to consume transaction from transaction-validation-input: {}", message, e);
                return null;
            }
        }).filter(Objects::nonNull).toList();

        if(null != txns && !txns.isEmpty() && txns.size() > 0) {
            List<RuleEngineEntity> ruleEngineEntity = new ArrayList<>();
            Map<String, RuleEngineEntity> map1 = cacheService.getAllRuleEngineEntities();
            if (map1.isEmpty()) {
                //Fetch rules from DB
                ruleEngineEntity = ruleEngineRepository.findAll();
                for (RuleEngineEntity res : ruleEngineEntity) {
                    cacheService.saveRuleEngineEntityToCache("fraud:ruleEngineEntities", res);
                }
            } else {
                for (Map.Entry<String, RuleEngineEntity> entry : map1.entrySet()) {
                    ruleEngineEntity.add(entry.getValue());
                }


                for (Transaction txn : txns) {
                    for (RuleEngineEntity re : ruleEngineEntity) {
                        if (txn.getAmount() < (double) re.getRiskScore()) {
                            //Send message to notification topic
                            String notificationMessage = "Transaction ID: " + txn.getTransactionId() + " matches rule: " + re.getRuleId();
                            notificationProducer.sendMessage(notificationMessage);
                            System.out.println("Sending notification: " + notificationMessage);
                        } else {
                            //Send message to notification topic
                            String notificationMessage = "Transaction ID: " + txn.getTransactionId() + " does not match rule: " + re.getRuleId();
                            notificationProducer.sendMessage(notificationMessage);
                            System.out.println("Sending notification: " + notificationMessage);
                        }
                    }
                }
            }
        }
    }
}
