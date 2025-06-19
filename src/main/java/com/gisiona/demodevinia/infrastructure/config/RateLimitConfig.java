package com.gisiona.demodevinia.infrastructure.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.github.bucket4j.Bucket;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class RateLimitConfig {

    private final RateLimitProperties rateLimitProperties;

    public RateLimitConfig(RateLimitProperties rateLimitProperties) {
        this.rateLimitProperties = rateLimitProperties;
    }

    @Bean
    public Cache<String, Bucket> rateLimitCache() {
        return Caffeine.newBuilder()
                .maximumSize(rateLimitProperties.getCacheSize())
                .expireAfterWrite(Duration.ofMinutes(rateLimitProperties.getCacheExpireMinutes()))
                .build();
    }
}
