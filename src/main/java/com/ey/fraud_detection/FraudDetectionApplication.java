package com.ey.fraud_detection;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication(scanBasePackages = "com.ey.fraud_detection")
@EnableKafka
@EnableCaching
public class FraudDetectionApplication {

	public static void main(String[] args) {

		SpringApplication.run(FraudDetectionApplication.class, args);
	}

}
