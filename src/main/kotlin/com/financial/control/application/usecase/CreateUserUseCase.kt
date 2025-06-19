package com.financial.control.application.usecase

import com.financial.control.application.dto.CreateUserRequest
import com.financial.control.application.port.PasswordEncoder
import com.financial.control.domain.model.User
import com.financial.control.domain.port.UserRepository
import org.springframework.stereotype.Service

@Service
class CreateUserUseCase(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {
    fun execute(request: CreateUserRequest): User {
        if (userRepository.existsByEmail(request.email)) {
            throw IllegalArgumentException("Email já está em uso")
        }
        
        if (userRepository.existsByCpf(request.cpf)) {
            throw IllegalArgumentException("CPF já está cadastrado")
        }

        val user = User(
            email = request.email,
            passwordHash = passwordEncoder.encode(request.password),
            firstName = request.firstName,
            lastName = request.lastName,
            cpf = request.cpf,
            phone = request.phone
        )

        return userRepository.save(user)
    }
}
