package com.financial.control.application.usecase

import com.financial.control.application.dto.UserResponse
import com.financial.control.domain.model.User
import com.financial.control.domain.port.UserRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class GetUserUseCase(
    private val userRepository: UserRepository
) {
    fun getUserById(id: Long): UserResponse {
        val user = userRepository.findActiveUserById(id)
            .orElseThrow { NoSuchElementException("Usuário não encontrado") }
        return user.toResponse()
    }

    fun getUserByEmail(email: String): UserResponse {
        val user = userRepository.findByEmail(email)
            .orElseThrow { NoSuchElementException("Usuário não encontrado") }
        return user.toResponse()
    }

    fun getAllUsers(): List<UserResponse> {
        return userRepository.findAllActiveUsers()
            .map { it.toResponse() }
    }
    
    private fun User.toResponse(): UserResponse = UserResponse(
        id = id,
        email = email,
        firstName = firstName,
        lastName = lastName,
        fullName = getFullName(),
        cpf = cpf,
        phone = phone,
        createdAt = createdAt,
        updatedAt = updatedAt,
        active = active
    )
}
