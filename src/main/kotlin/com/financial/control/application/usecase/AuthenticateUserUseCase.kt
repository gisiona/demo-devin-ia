package com.financial.control.application.usecase

import com.financial.control.application.dto.LoginRequest
import com.financial.control.application.dto.LoginResponse
import com.financial.control.application.dto.UserResponse
import com.financial.control.application.port.JwtTokenProvider
import com.financial.control.application.port.PasswordEncoder
import com.financial.control.domain.model.User
import com.financial.control.domain.port.UserRepository
import org.springframework.stereotype.Service

@Service
class AuthenticateUserUseCase(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtTokenProvider: JwtTokenProvider
) {
    fun execute(request: LoginRequest): LoginResponse {
        val user = userRepository.findByEmail(request.email)
            .orElseThrow { IllegalArgumentException("Credenciais inválidas") }

        if (!user.active) {
            throw IllegalArgumentException("Usuário inativo")
        }

        if (!passwordEncoder.matches(request.password, user.passwordHash)) {
            throw IllegalArgumentException("Credenciais inválidas")
        }

        val token = jwtTokenProvider.generateToken(user.email)
        return LoginResponse(
            token = token,
            user = user.toResponse()
        )
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
