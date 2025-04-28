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
	implementation("org.springframework.boot:spring-boot-starter-data-redis")
	implementation("org.springframework.boot:spring-boot-starter-cache")
	implementation("javax.validation:validation-api:2.0.1.Final")
	implementation("org.apache.commons:commons-lang3:3.12.0")
	implementation("org.postgresql:postgresql:42.7.5")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.4.5")
	implementation("com.opencsv:opencsv:5.10")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.6")

	compileOnly("org.projectlombok:lombok:1.18.38")
	annotationProcessor("org.projectlombok:lombok")

	implementation("org.springframework.kafka:spring-kafka:3.3.4")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
