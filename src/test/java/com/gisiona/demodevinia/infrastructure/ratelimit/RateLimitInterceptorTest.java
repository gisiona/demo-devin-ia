package com.gisiona.demodevinia.infrastructure.ratelimit;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RateLimitInterceptorTest {

    @Mock
    private RateLimitService rateLimitService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    private RateLimitInterceptor interceptor;

    @BeforeEach
    void setUp() {
        interceptor = new RateLimitInterceptor(rateLimitService);
    }

    @Test
    void shouldAllowRequestWhenRateLimitNotExceeded() throws Exception {
        when(request.getRemoteAddr()).thenReturn("127.0.0.1");
        when(request.getRequestURI()).thenReturn("/api/users");
        when(request.getMethod()).thenReturn("GET");
        when(rateLimitService.tryConsume(anyString())).thenReturn(true);
        when(rateLimitService.getAvailableTokens(anyString())).thenReturn(10L);

        boolean result = interceptor.preHandle(request, response, null);

        assertTrue(result);
        verify(response).setHeader("X-Rate-Limit-Remaining", "10");
    }

    @Test
    void shouldRejectRequestWhenRateLimitExceeded() throws Exception {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        when(request.getRemoteAddr()).thenReturn("127.0.0.1");
        when(request.getRequestURI()).thenReturn("/api/users");
        when(request.getMethod()).thenReturn("GET");
        when(rateLimitService.tryConsume(anyString())).thenReturn(false);
        when(response.getWriter()).thenReturn(printWriter);

        boolean result = interceptor.preHandle(request, response, null);

        assertFalse(result);
        verify(response).setStatus(429);
        verify(response).setContentType("application/json");
        
        String responseBody = stringWriter.toString();
        assertTrue(responseBody.contains("Muitas requisições"));
    }

    @Test
    void shouldExtractIpFromXForwardedForHeader() throws Exception {
        when(request.getHeader("X-Forwarded-For")).thenReturn("192.168.1.1, 10.0.0.1");
        when(request.getRequestURI()).thenReturn("/api/users");
        when(request.getMethod()).thenReturn("GET");
        when(rateLimitService.tryConsume("rate_limit:192.168.1.1")).thenReturn(true);
        when(rateLimitService.getAvailableTokens("rate_limit:192.168.1.1")).thenReturn(5L);

        boolean result = interceptor.preHandle(request, response, null);

        assertTrue(result);
        verify(rateLimitService).tryConsume("rate_limit:192.168.1.1");
        verify(response).setHeader("X-Rate-Limit-Remaining", "5");
    }

    @Test
    void shouldExtractIpFromXRealIpHeader() throws Exception {
        when(request.getHeader("X-Forwarded-For")).thenReturn(null);
        when(request.getHeader("X-Real-IP")).thenReturn("192.168.1.2");
        when(request.getRequestURI()).thenReturn("/api/users");
        when(request.getMethod()).thenReturn("GET");
        when(rateLimitService.tryConsume("rate_limit:192.168.1.2")).thenReturn(true);
        when(rateLimitService.getAvailableTokens("rate_limit:192.168.1.2")).thenReturn(8L);

        boolean result = interceptor.preHandle(request, response, null);

        assertTrue(result);
        verify(rateLimitService).tryConsume("rate_limit:192.168.1.2");
        verify(response).setHeader("X-Rate-Limit-Remaining", "8");
    }

    @Test
    void shouldFallbackToRemoteAddrWhenNoHeaders() throws Exception {
        when(request.getHeader("X-Forwarded-For")).thenReturn(null);
        when(request.getHeader("X-Real-IP")).thenReturn(null);
        when(request.getRemoteAddr()).thenReturn("127.0.0.1");
        when(rateLimitService.tryConsume("rate_limit:127.0.0.1")).thenReturn(true);
        when(rateLimitService.getAvailableTokens("rate_limit:127.0.0.1")).thenReturn(15L);

        boolean result = interceptor.preHandle(request, response, null);

        assertTrue(result);
        verify(rateLimitService).tryConsume("rate_limit:127.0.0.1");
    }
}
