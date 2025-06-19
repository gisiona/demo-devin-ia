package com.gisiona.demodevinia.infrastructure.web.dto

import com.gisiona.demodevinia.domain.model.User
import java.time.LocalDateTime
import java.util.UUID

data class UserResponse(
    val id: UUID,
    val name: String,
    val email: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    companion object {
        fun from(user: User): UserResponse {
            return UserResponse(
                id = user.id,
                name = user.name,
                email = user.email,
                createdAt = user.createdAt,
                updatedAt = user.updatedAt
            )
        }
    }
}
