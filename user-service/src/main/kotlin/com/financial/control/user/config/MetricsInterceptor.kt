package com.financial.control.user.config

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Component
class MetricsInterceptor(
    private val userMetricsService: UserMetricsService
) : HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        request.setAttribute("startTime", System.currentTimeMillis())
        return true
    }

    override fun afterCompletion(request: HttpServletRequest, response: HttpServletResponse, handler: Any, ex: Exception?) {
        val authentication = SecurityContextHolder.getContext().authentication
        if (authentication != null && authentication.isAuthenticated && authentication.name != "anonymousUser") {
            val userEmail = authentication.name
            val endpoint = request.requestURI
            val startTime = request.getAttribute("startTime") as Long
            val duration = System.currentTimeMillis() - startTime

            userMetricsService.incrementUserRequest(userEmail, endpoint)
            userMetricsService.recordRequestDuration(userEmail, endpoint, duration)
        }
    }
}

@Component
class WebConfig(
    private val metricsInterceptor: MetricsInterceptor
) : WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(metricsInterceptor)
            .addPathPatterns("/api/v1/**")
            .excludePathPatterns("/api/v1/users/register", "/api/v1/users/login")
    }
}
