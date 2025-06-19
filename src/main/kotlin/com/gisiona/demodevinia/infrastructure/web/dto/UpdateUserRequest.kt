package com.gisiona.demodevinia.infrastructure.web.dto

import jakarta.validation.constraints.Email

data class UpdateUserRequest(
    val name: String?,
    
    @field:Email(message = "Email must be valid")
    val email: String?
)
