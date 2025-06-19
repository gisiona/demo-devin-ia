package com.gisiona.demodevinia.infrastructure.ratelimit;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDateTime;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(RateLimitInterceptor.class);
    private final RateLimitService rateLimitService;

    public RateLimitInterceptor(RateLimitService rateLimitService) {
        this.rateLimitService = rateLimitService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String clientIp = getClientIpAddress(request);
        String key = "rate_limit:" + clientIp;
        
        MDC.put("clientIp", clientIp);
        MDC.put("requestUri", request.getRequestURI());
        MDC.put("httpMethod", request.getMethod());

        long availableTokens = rateLimitService.getAvailableTokens(key);
        
        if (!rateLimitService.tryConsume(key)) {
            logger.warn("Rate limit exceeded for client IP: {} on endpoint: {} {}", 
                clientIp, request.getMethod(), request.getRequestURI());
            
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType("application/json");
            response.getWriter().write(String.format(
                "{\"status\":%d,\"message\":\"%s\",\"timestamp\":\"%s\"}",
                HttpStatus.TOO_MANY_REQUESTS.value(),
                "Muitas requisições. Tente novamente mais tarde.",
                LocalDateTime.now().toString()
            ));
            return false;
        }

        availableTokens = rateLimitService.getAvailableTokens(key);
        response.setHeader("X-Rate-Limit-Remaining", String.valueOf(availableTokens));
        
        logger.debug("Rate limit check passed for client IP: {} on endpoint: {} {}, remaining tokens: {}", 
            clientIp, request.getMethod(), request.getRequestURI(), availableTokens);
        
        return true;
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
    }
}
