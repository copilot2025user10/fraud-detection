plugins {
	java
	id("org.springframework.boot") version "3.4.4"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.ey"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-web")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	implementation("org.postgresql:postgresql:42.6.0")
	implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
	implementation("jakarta.servlet:jakarta.servlet-api:6.0.0")
	implementation("jakarta.validation:jakarta.validation-api:3.0.2")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")

	implementation("org.springframework.kafka:spring-kafka:3.3.4")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
