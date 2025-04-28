package com.ey.fraud_detection.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.Duration;

@Configuration
public class RedisConfig {

    @Value(value = "${spring.redis.host}")
    private String redisHost;

    @Value(value = "${spring.redis.port}")
    private String redisPort;

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {

        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(redisHost);
        config.setPort(Integer.parseInt(redisPort));
        LettuceClientConfiguration clientConfig = LettuceClientConfiguration
                .builder()
                .commandTimeout(Duration.ofSeconds(360))
                .shutdownTimeout(Duration.ofSeconds(1))
                .build();
        return new LettuceConnectionFactory(config,clientConfig);
    }

    @Bean
    public StringRedisTemplate redisTemplate() {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(redisConnectionFactory());
        return template;
    }
}
