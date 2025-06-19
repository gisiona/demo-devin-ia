package com.financial.control.gateway.controller

import com.financial.control.gateway.config.ServiceUrlsProperties
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.RestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import jakarta.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = ["*"])
class GatewayController(
    private val serviceUrls: ServiceUrlsProperties,
    private val restTemplate: RestTemplate
) {
    
    init {
        println("DEBUG: GatewayController initialized")
        println("DEBUG: User service URL: ${serviceUrls.userService.url}")
    }

    @RequestMapping("/users/**", method = [RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE])
    fun proxyUserService(
        request: HttpServletRequest,
        @RequestBody(required = false) body: Any?
    ): ResponseEntity<Any> {
        println("DEBUG: proxyUserService called with URI: ${request.requestURI}")
        val path = request.requestURI.removePrefix("/api/v1")
        val targetUrl = "${serviceUrls.userService.url}/api/v1$path"
        
        val headers = HttpHeaders()
        request.headerNames.asSequence().forEach { headerName: String ->
            headers.add(headerName, request.getHeader(headerName))
        }
        
        val entity = HttpEntity(body, headers)
        val method = HttpMethod.valueOf(request.method)
        
        return try {
            println("DEBUG: Forwarding request to: $targetUrl")
            println("DEBUG: Method: $method")
            println("DEBUG: Headers: $headers")
            println("DEBUG: Body: $body")
            val response = restTemplate.exchange(targetUrl, method, entity, Any::class.java)
            println("DEBUG: Response status: ${response.statusCode}")
            response
        } catch (e: Exception) {
            println("DEBUG: Exception occurred: ${e.javaClass.simpleName}: ${e.message}")
            e.printStackTrace()
            ResponseEntity.status(500).body(mapOf("error" to "Service unavailable: ${e.message}"))
        }
    }

    @RequestMapping("/transactions/**", method = [RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE])
    fun proxyTransactionService(
        request: HttpServletRequest,
        @RequestBody(required = false) body: Any?
    ): ResponseEntity<Any> {
        val path = request.requestURI.removePrefix("/api/v1")
        val targetUrl = "${serviceUrls.transactionService.url}/api/v1$path"
        
        val headers = HttpHeaders()
        request.headerNames.asSequence().forEach { headerName: String ->
            headers.add(headerName, request.getHeader(headerName))
        }
        
        val entity = HttpEntity(body, headers)
        val method = HttpMethod.valueOf(request.method)
        
        return try {
            restTemplate.exchange(targetUrl, method, entity, Any::class.java)
        } catch (e: Exception) {
            ResponseEntity.status(500).body(mapOf("error" to "Service unavailable: ${e.message}"))
        }
    }

    @RequestMapping("/investments/**", method = [RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE])
    fun proxyInvestmentService(
        request: HttpServletRequest,
        @RequestBody(required = false) body: Any?
    ): ResponseEntity<Any> {
        val path = request.requestURI.removePrefix("/api/v1")
        val targetUrl = "${serviceUrls.investmentService.url}/api/v1$path"
        
        val headers = HttpHeaders()
        request.headerNames.asSequence().forEach { headerName: String ->
            headers.add(headerName, request.getHeader(headerName))
        }
        
        val entity = HttpEntity(body, headers)
        val method = HttpMethod.valueOf(request.method)
        
        return try {
            restTemplate.exchange(targetUrl, method, entity, Any::class.java)
        } catch (e: Exception) {
            ResponseEntity.status(500).body(mapOf("error" to "Service unavailable: ${e.message}"))
        }
    }
}
