package com.ey.fraud_detection.repository;

import com.ey.fraud_detection.entity.RuleEngineEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RuleEngineRepository extends JpaRepository<RuleEngineEntity, Long> {
    @Query("SELECT r FROM RuleEngineEntity r WHERE r.columnName = :columnName")
    List<RuleEngineEntity> findByColumnName(String columnName);
}