package com.gisiona.demodevinia.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "rate-limit")
public class RateLimitProperties {

    private int requestsPerHour = 100;
    private int requestsPerMinute = 20;
    private int cacheSize = 100000;
    private int cacheExpireMinutes = 10;

    public int getRequestsPerHour() {
        return requestsPerHour;
    }

    public void setRequestsPerHour(int requestsPerHour) {
        this.requestsPerHour = requestsPerHour;
    }

    public int getRequestsPerMinute() {
        return requestsPerMinute;
    }

    public void setRequestsPerMinute(int requestsPerMinute) {
        this.requestsPerMinute = requestsPerMinute;
    }

    public int getCacheSize() {
        return cacheSize;
    }

    public void setCacheSize(int cacheSize) {
        this.cacheSize = cacheSize;
    }

    public int getCacheExpireMinutes() {
        return cacheExpireMinutes;
    }

    public void setCacheExpireMinutes(int cacheExpireMinutes) {
        this.cacheExpireMinutes = cacheExpireMinutes;
    }
}
