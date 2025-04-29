package com.ey.fraud_detection.service;

import com.ey.fraud_detection.entity.RuleEngineEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

@Service
public class CachingService {

    @Autowired
    private RedisTemplate<String, RuleEngineEntity> redisTemplate;

    public Map<String, RuleEngineEntity> getAllRuleEngineEntities() {
        Map<Object,Object> rawEntries= redisTemplate.opsForHash().entries("fraud:ruleEngineEntities");
        Map<String, RuleEngineEntity> ruleEngineEntities = new HashMap<>();

        for (Map.Entry<Object, Object> entry : rawEntries.entrySet()) {
            ruleEngineEntities.put(entry.getKey().toString(), (RuleEngineEntity) entry.getValue());
        }
        return ruleEngineEntities;

    }

    public void saveRuleEngineEntityToCache(String ruleEngineEntityId, RuleEngineEntity ruleEngineEntity) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String ruleEngineEntityJson = objectMapper.writeValueAsString(ruleEngineEntity);
            redisTemplate.opsForHash().put("fraud:ruleEngineEntities", ruleEngineEntityId, ruleEngineEntityJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize RuleEngineEntity object", e);
        }
    }

   }
