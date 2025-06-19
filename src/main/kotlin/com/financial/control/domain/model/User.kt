package com.financial.control.domain.model

import java.time.LocalDateTime

data class User(
    val id: Long = 0,
    val email: String,
    val passwordHash: String,
    val firstName: String,
    val lastName: String,
    val cpf: String,
    val phone: String? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    val active: Boolean = true
) {
    fun getFullName(): String = "$firstName $lastName"
    
    fun isValidForUpdate(): Boolean = active && email.isNotBlank()
    
    fun isActive(): Boolean = active
}
