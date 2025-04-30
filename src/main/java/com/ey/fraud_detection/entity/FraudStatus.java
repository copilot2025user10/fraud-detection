package com.ey.fraud_detection.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.sql.Timestamp;

@Entity
@Data
@Table(name = "fraudstatus")
public class FraudStatus {

    @Id
    @Column(name = "txnid")
    private String txnid;

    @Column(name = "riskscore")
    private Integer riskscore;

}