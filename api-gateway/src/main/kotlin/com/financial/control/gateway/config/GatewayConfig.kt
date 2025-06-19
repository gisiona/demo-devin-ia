package com.financial.control.gateway.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(ServiceUrlsProperties::class)
class GatewayConfig

@ConfigurationProperties(prefix = "services")
data class ServiceUrlsProperties(
    val userService: ServiceUrl = ServiceUrl(),
    val transactionService: ServiceUrl = ServiceUrl(),
    val investmentService: ServiceUrl = ServiceUrl()
) {
    data class ServiceUrl(
        val url: String = ""
    )
}
