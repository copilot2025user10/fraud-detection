package com.ey.fraud_detection.controller;

import com.ey.fraud_detection.consumer.TransactionForCheckConsumer;
import com.ey.fraud_detection.entity.RuleEngineEntity;
import com.ey.fraud_detection.service.RuleEngineService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RuleEngineController {

    @Autowired
    RuleEngineService service;

    @Autowired
    TransactionForCheckConsumer tx;

    public RuleEngineController(RuleEngineService service) {
        this.service = service;
    }

    @GetMapping("/entities")
    public List<RuleEngineEntity> getEntities() {
        return service.getAllEntities();
    }

    @GetMapping("/rules")
    public List<RuleEngineEntity> getRulesByColumnName(@RequestParam String columnName) {
        return service.getRulesByColumnName(columnName);
    }

    @GetMapping("/test")
    public void test() throws JsonProcessingException {
        tx.listen(null);
    }


    @GetMapping("/processRules")
    public List<String> processRulesByColumnName(@RequestParam String columnName) {
        List<RuleEngineEntity> rules = service.getRulesByColumnName(columnName);
        return service.processRules(rules);
    }
}