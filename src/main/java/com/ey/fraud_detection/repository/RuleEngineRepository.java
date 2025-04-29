package com.ey.fraud_detection.repository;

import com.ey.fraud_detection.entity.RuleEngineEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RuleEngineRepository extends JpaRepository<RuleEngineEntity, Integer> {
    //@Query("SELECT r FROM ruleDefinition r")
    //List<RuleEngineEntity> findRules();
}