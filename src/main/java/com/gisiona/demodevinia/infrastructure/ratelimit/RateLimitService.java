package com.gisiona.demodevinia.infrastructure.ratelimit;

import com.github.benmanes.caffeine.cache.Cache;
import com.gisiona.demodevinia.infrastructure.config.RateLimitProperties;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RateLimitService {

    private final Cache<String, Bucket> cache;
    private final RateLimitProperties rateLimitProperties;

    public RateLimitService(Cache<String, Bucket> cache, RateLimitProperties rateLimitProperties) {
        this.cache = cache;
        this.rateLimitProperties = rateLimitProperties;
    }

    public Bucket createNewBucket() {
        return Bucket.builder()
                .addLimit(Bandwidth.classic(
                    rateLimitProperties.getRequestsPerHour(), 
                    Refill.intervally(rateLimitProperties.getRequestsPerHour(), Duration.ofHours(1))
                ))
                .addLimit(Bandwidth.classic(
                    rateLimitProperties.getRequestsPerMinute(), 
                    Refill.intervally(rateLimitProperties.getRequestsPerMinute(), Duration.ofMinutes(1))
                ))
                .build();
    }

    public Bucket resolveBucket(String key) {
        return cache.get(key, k -> createNewBucket());
    }

    public boolean tryConsume(String key) {
        return resolveBucket(key).tryConsume(1);
    }

    public long getAvailableTokens(String key) {
        return resolveBucket(key).getAvailableTokens();
    }
}
