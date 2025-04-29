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
@Table(name = "ruledefinition")
public class RuleEngineEntity {

    @Id
    @Column(name = "ruleid")
    private Integer ruleId;

    @Column(name = "ruletype")
    private String ruleType;

    @Column(name = "ruledescription")
    private String ruleDescription;

    @Column(name = "riskscore")
    private Integer riskScore;

    @Column(name = "rulecreationdate")
    private Timestamp ruleCreationDate;

    @Column(name = "rulevalue")
    private String ruleValue;

}