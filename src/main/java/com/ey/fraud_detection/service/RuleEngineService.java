package com.ey.fraud_detection.service;

import com.ey.fraud_detection.entity.RuleEngineEntity;
import com.ey.fraud_detection.repository.RuleEngineRepository;
import com.ey.fraud_detection.utils.RuleEngineUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RuleEngineService {

    final RuleEngineRepository repository;

    public RuleEngineService(RuleEngineRepository repository) {
        this.repository = repository;
    }

    public List<RuleEngineEntity> getAllEntities() {
        return repository.findAll();
    }

    public List<RuleEngineEntity> getRulesByColumnName(String columnName) {
        return repository.findByColumnName(columnName);
    }

    public List<String> processRules(List<RuleEngineEntity> rules) {
        List<String> processedRules = new ArrayList<>();
        RuleEngineUtils reUtils = new RuleEngineUtils();
        for (RuleEngineEntity rule : rules) {
            // Example business logic: Append a prefix to the rule name
        //    String processedRule = "Processed_" + rule.getColumnName();
            String ruleResult = reUtils.ruleResult(10000);
         //   processedRules.add(processedRule);
        }
        return processedRules;
    }
}