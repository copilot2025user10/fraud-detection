package com.ey.fraud_detection.repository;

import com.ey.fraud_detection.entity.FraudStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FraudStatusRepository extends JpaRepository<FraudStatus, Integer> {
    //@Query("SELECT r FROM ruleDefinition r")
    //List<RuleEngineEntity> findRules();
}