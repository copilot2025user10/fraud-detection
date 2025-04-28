package com.ey.fraud_detection.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ruleDefinition")
public class RuleEngineEntity {

    @Id
    @Column(name = "ruleid")
    private String ruleId;

    @Column(name = "ruleType")
    private String ruleType;

    @Column(name = "ruleDescription")
    private String ruleDescription;

    @Column(name = "riskScore")
    private Integer riskScore;

    @Column(name = "ruleCreationDate")
    private Timestamp ruleCreationDate;

    @Column(name = "ruleValue")
    private String ruleValue;

}