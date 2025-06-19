package com.gisiona.demodevinia.infrastructure.web.dto

data class ErrorResponse(
    val message: String,
    val timestamp: String = java.time.LocalDateTime.now().toString()
)
