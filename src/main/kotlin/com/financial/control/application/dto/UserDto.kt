package com.financial.control.application.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

data class CreateUserRequest(
    @field:Email(message = "Email deve ter um formato válido")
    @field:NotBlank(message = "Email é obrigatório")
    val email: String,

    @field:NotBlank(message = "Senha é obrigatória")
    @field:Size(min = 8, message = "Senha deve ter pelo menos 8 caracteres")
    val password: String,

    @field:NotBlank(message = "Nome é obrigatório")
    @field:Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    val firstName: String,

    @field:NotBlank(message = "Sobrenome é obrigatório")
    @field:Size(max = 100, message = "Sobrenome deve ter no máximo 100 caracteres")
    val lastName: String,

    @field:NotBlank(message = "CPF é obrigatório")
    @field:Size(min = 11, max = 14, message = "CPF deve ter entre 11 e 14 caracteres")
    val cpf: String,

    val phone: String? = null
)

data class UpdateUserRequest(
    @field:NotBlank(message = "Nome é obrigatório")
    @field:Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    val firstName: String,

    @field:NotBlank(message = "Sobrenome é obrigatório")
    @field:Size(max = 100, message = "Sobrenome deve ter no máximo 100 caracteres")
    val lastName: String,

    val phone: String? = null
)

data class UserResponse(
    val id: Long,
    val email: String,
    val firstName: String,
    val lastName: String,
    val fullName: String,
    val cpf: String,
    val phone: String?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val active: Boolean
)

data class LoginRequest(
    @field:Email(message = "Email deve ter um formato válido")
    @field:NotBlank(message = "Email é obrigatório")
    val email: String,

    @field:NotBlank(message = "Senha é obrigatória")
    val password: String
)

data class LoginResponse(
    val token: String,
    val type: String = "Bearer",
    val user: UserResponse
)

data class ChangePasswordRequest(
    @field:NotBlank(message = "Senha atual é obrigatória")
    val currentPassword: String,

    @field:NotBlank(message = "Nova senha é obrigatória")
    @field:Size(min = 8, message = "Nova senha deve ter pelo menos 8 caracteres")
    val newPassword: String
)
