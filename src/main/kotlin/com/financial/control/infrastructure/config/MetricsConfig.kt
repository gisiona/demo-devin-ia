package com.financial.control.infrastructure.config

import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Timer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component

@Configuration
class MetricsConfig {

    @Bean
    fun userRequestCounter(meterRegistry: MeterRegistry): Counter {
        return Counter.builder("user_requests_total")
            .description("Total number of requests per user")
            .register(meterRegistry)
    }

    @Bean
    fun userRequestTimer(meterRegistry: MeterRegistry): Timer {
        return Timer.builder("user_request_duration")
            .description("Request duration per user")
            .register(meterRegistry)
    }
}

@Component
class UserMetricsService(
    private val meterRegistry: MeterRegistry
) {
    fun incrementUserRequest(user: String) {
        Counter.builder("user_requests_total")
            .tag("user", user)
            .register(meterRegistry)
            .increment()
    }

    fun recordRequestDuration(user: String, duration: Long) {
        Timer.builder("user_request_duration")
            .tag("user", user)
            .register(meterRegistry)
            .record(duration, java.util.concurrent.TimeUnit.MILLISECONDS)
    }
}
