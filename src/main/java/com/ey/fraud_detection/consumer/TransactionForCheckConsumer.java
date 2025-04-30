package com.ey.fraud_detection.consumer;

import com.ey.fraud_detection.entity.FraudStatus;
import com.ey.fraud_detection.entity.RuleEngineEntity;
import com.ey.fraud_detection.model.Transaction;
import com.ey.fraud_detection.producer.NotificationProducer;
import com.ey.fraud_detection.repository.FraudStatusRepository;
import com.ey.fraud_detection.repository.RuleEngineRepository;
import com.ey.fraud_detection.service.CachingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.*;

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
    private final FraudStatusRepository fraudStatusRepository;

    public TransactionForCheckConsumer(
            RedisTemplate<String, RuleEngineEntity> redisTemplate,
            CachingService cacheService,
            RuleEngineRepository ruleEngineRepository,
            NotificationProducer notificationProducer, FraudStatusRepository fraudStatusRepository
    ) {
        this.redisTemplate = redisTemplate;
        this.cacheService = cacheService;
        this.ruleEngineRepository = ruleEngineRepository;
        this.notificationProducer = notificationProducer;
        this.fraudStatusRepository = fraudStatusRepository;
    }

//    final NotificationProducer notificationProducer;

//    public TransactionForCheckConsumer(NotificationProducer notificationProducer) {
//        this.notificationProducer = notificationProducer;
//    }

    @KafkaListener(topics = "transaction-valid-output", groupId = "fraud-detection-group")
    public void listen(List<String> message) throws JsonProcessingException {
        System.out.println("Received Message: " + message);

       /* List<String> list1 = new ArrayList<>();
        list1.add("{\"transactionId\":\"TXN12345\",\"userId\":\"USER001\",\"amount\":100.5,\"currency\":\"USD\",\"timestamp\":1696155330000,\"merchantId\":\"MERCHANT001\",\"location\":\"New York\",\"userRiskScore\":4.8285239146807415,\"merchantRiskScore\":8.845839367221298,\"validationStatus\":\"VALID\"}");
        list1.add("{\"transactionId\":\"TXN12346\",\"userId\":\"USER002\",\"amount\":250.0,\"currency\":\"EUR\",\"timestamp\":1696249245000,\"merchantId\":\"MERCHANT002\",\"location\":\"London\",\"userRiskScore\":3.7136342842774352,\"merchantRiskScore\":8.183467433363266,\"validationStatus\":\"VALID\"}");
        list1.add("{\"transactionId\":\"TXN12347\",\"userId\":\"USER003\",\"amount\":75.25,\"currency\":\"INR\",\"timestamp\":1696343400000,\"merchantId\":\"MERCHANT003\",\"location\":\"Mumbai\",\"userRiskScore\":4.1058280017170725,\"merchantRiskScore\":9.591102108998971,\"validationStatus\":\"VALID\"}");
        list1.add("{\"transactionId\":\"TXN12349\",\"userId\":\"USER005\",\"amount\":50.0,\"currency\":\"AUD\",\"timestamp\":1696528800000,\"merchantId\":\"MERCHANT005\",\"location\":\"Sydney\",\"userRiskScore\":6.899749972551875,\"merchantRiskScore\":8.600171897848295,\"validationStatus\":\"VALID\"}");
*/
        //Add business logic
        //List<Transaction> txns = message.stream().map(m -> {
        List<Transaction> txns = message.stream().map(m -> {
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
                    cacheService.saveRuleEngineEntityToCache(res.getRuleId().toString(), res);
                }
            } else {
                for (Map.Entry<String, RuleEngineEntity> entry : map1.entrySet()) {
                    ruleEngineEntity.add(entry.getValue());
                }
            }

            List<String> notifList = new ArrayList<>();
            for (Transaction txn : txns)
            {
                boolean isFraud = false;
                int riskScore = 0;
                List<FraudStatus> fraudStatuses = new ArrayList<>();
                for (RuleEngineEntity re : ruleEngineEntity)
                {
                    if(re.getRuleType().equalsIgnoreCase("AMOUNT")) {
                        if (txn.getAmount() > Double.parseDouble(re.getRuleValue())) {
                            riskScore = riskScore + re.getRiskScore();
                            isFraud = true;
                        }
                    }
                    if(re.getRuleType().equalsIgnoreCase("Payment Gateway"))
                    {
                        if (txn.getMerchantId().equalsIgnoreCase(re.getRuleValue())) {
                            riskScore = riskScore + re.getRiskScore();
                            isFraud = true;
                        }
                    }
                    if(re.getRuleType().equalsIgnoreCase("Location"))
                    {
                        if (txn.getLocation().equalsIgnoreCase(re.getRuleValue())) {
                            riskScore = riskScore + re.getRiskScore();
                            isFraud = true;
                        }
                    }
                }
                if(riskScore > 0)
                {
                    FraudStatus fraudStatus = new FraudStatus();
                    fraudStatus.setTxnid(txn.getTransactionId());
                    fraudStatus.setRiskscore(riskScore);
                    fraudStatuses.add(fraudStatus);
                    fraudStatusRepository.saveAll(fraudStatuses);

                    //Send message to notification topic
                    String notificationMessage = "Transaction ID: " + txn.getTransactionId() + " has a total riskScore of: " + riskScore + ". Fraud Detected.";
                    System.out.println("Sending notification: " + notificationMessage);
                    notifList.add(notificationMessage);
                }
                if(!isFraud) {
                    //Send message to notification topic
                    String notificationMessage = "Transaction ID: " + txn.getTransactionId() + " does not match any rule. No Fraud Detected.";
                    System.out.println("Sending notification: " + notificationMessage);
                    //notificationProducer.sendMessage(notificationMessage);
                    notifList.add(notificationMessage);
                }
            }
            for(String msg : notifList) {
                notificationProducer.sendMessage(msg);
            }
        }
    }
}
