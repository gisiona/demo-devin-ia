package com.gisiona.demodevinia.infrastructure.ratelimit;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.gisiona.demodevinia.infrastructure.config.RateLimitProperties;
import io.github.bucket4j.Bucket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class RateLimitServiceTest {

    private RateLimitService rateLimitService;
    private Cache<String, Bucket> cache;
    private RateLimitProperties rateLimitProperties;

    @BeforeEach
    void setUp() {
        cache = Caffeine.newBuilder()
                .maximumSize(100)
                .expireAfterWrite(Duration.ofMinutes(1))
                .build();
        
        rateLimitProperties = new RateLimitProperties();
        rateLimitProperties.setRequestsPerHour(100);
        rateLimitProperties.setRequestsPerMinute(20);
        
        rateLimitService = new RateLimitService(cache, rateLimitProperties);
    }

    @Test
    void shouldCreateNewBucket() {
        Bucket bucket = rateLimitService.createNewBucket();
        
        assertNotNull(bucket);
        assertTrue(bucket.getAvailableTokens() > 0);
    }

    @Test
    void shouldResolveBucketForKey() {
        String key = "test-key";
        
        Bucket bucket1 = rateLimitService.resolveBucket(key);
        Bucket bucket2 = rateLimitService.resolveBucket(key);
        
        assertSame(bucket1, bucket2);
    }

    @Test
    void shouldConsumeTokensSuccessfully() {
        String key = "test-consume";
        
        assertTrue(rateLimitService.tryConsume(key));
        
        long tokensAfter = rateLimitService.getAvailableTokens(key);
        assertTrue(tokensAfter >= 0);
    }

    @Test
    void shouldRejectWhenRateLimitExceeded() {
        String key = "test-exceed";
        
        for (int i = 0; i < 25; i++) {
            rateLimitService.tryConsume(key);
        }
        
        assertFalse(rateLimitService.tryConsume(key));
    }

    @Test
    void shouldTrackAvailableTokens() {
        String key = "test-tokens";
        
        long initialTokens = rateLimitService.getAvailableTokens(key);
        rateLimitService.tryConsume(key);
        long tokensAfterConsume = rateLimitService.getAvailableTokens(key);
        
        assertEquals(initialTokens - 1, tokensAfterConsume);
    }
}
