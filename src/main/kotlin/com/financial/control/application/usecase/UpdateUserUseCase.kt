package com.financial.control.application.usecase

import com.financial.control.application.dto.ChangePasswordRequest
import com.financial.control.application.dto.UpdateUserRequest
import com.financial.control.application.dto.UserResponse
import com.financial.control.application.port.PasswordEncoder
import com.financial.control.domain.model.User
import com.financial.control.domain.port.UserRepository
import org.springframework.stereotype.Service

@Service
class UpdateUserUseCase(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {
    fun updateUser(id: Long, request: UpdateUserRequest): UserResponse {
        val user = userRepository.findActiveUserById(id)
            .orElseThrow { NoSuchElementException("Usuário não encontrado") }

        val updatedUser = user.copy(
            firstName = request.firstName,
            lastName = request.lastName,
            phone = request.phone
        )

        val savedUser = userRepository.save(updatedUser)
        return savedUser.toResponse()
    }

    fun changePassword(id: Long, request: ChangePasswordRequest) {
        val user = userRepository.findActiveUserById(id)
            .orElseThrow { NoSuchElementException("Usuário não encontrado") }

        if (!passwordEncoder.matches(request.currentPassword, user.passwordHash)) {
            throw IllegalArgumentException("Senha atual incorreta")
        }

        val updatedUser = user.copy(passwordHash = passwordEncoder.encode(request.newPassword))
        userRepository.save(updatedUser)
    }

    fun deactivateUser(id: Long) {
        val user = userRepository.findActiveUserById(id)
            .orElseThrow { NoSuchElementException("Usuário não encontrado") }

        val deactivatedUser = user.copy(active = false)
        userRepository.save(deactivatedUser)
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
