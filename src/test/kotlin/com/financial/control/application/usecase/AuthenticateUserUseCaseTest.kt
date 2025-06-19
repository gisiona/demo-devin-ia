package com.financial.control.application.usecase

import com.financial.control.application.dto.LoginRequest
import com.financial.control.application.port.JwtTokenProvider
import com.financial.control.application.port.PasswordEncoder
import com.financial.control.domain.model.User
import com.financial.control.domain.port.UserRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows
import java.util.*

class AuthenticateUserUseCaseTest {

    private val userRepository = mockk<UserRepository>()
    private val passwordEncoder = mockk<PasswordEncoder>()
    private val jwtTokenProvider = mockk<JwtTokenProvider>()
    private val authenticateUserUseCase = AuthenticateUserUseCase(userRepository, passwordEncoder, jwtTokenProvider)

    @Test
    fun `should authenticate user successfully with valid credentials`() {
        val request = LoginRequest(
            email = "test@test.com",
            password = "password123"
        )

        val user = User(
            id = 1L,
            email = request.email,
            passwordHash = "encodedPassword",
            firstName = "João",
            lastName = "Silva",
            cpf = "12345678901",
            active = true
        )

        every { userRepository.findByEmail(request.email) } returns Optional.of(user)
        every { passwordEncoder.matches(request.password, user.passwordHash) } returns true
        every { jwtTokenProvider.generateToken(user.email) } returns "jwt-token"

        val result = authenticateUserUseCase.execute(request)

        assertEquals("jwt-token", result.token)
        assertEquals("Bearer", result.type)
        assertEquals(user.email, result.user.email)
    }

    @Test
    fun `should throw exception when user not found`() {
        val request = LoginRequest(
            email = "test@test.com",
            password = "password123"
        )

        every { userRepository.findByEmail(request.email) } returns Optional.empty()

        val exception = assertThrows<IllegalArgumentException> {
            authenticateUserUseCase.execute(request)
        }

        assertEquals("Credenciais inválidas", exception.message)
    }

    @Test
    fun `should throw exception when user is inactive`() {
        val request = LoginRequest(
            email = "test@test.com",
            password = "password123"
        )

        val user = User(
            id = 1L,
            email = request.email,
            passwordHash = "encodedPassword",
            firstName = "João",
            lastName = "Silva",
            cpf = "12345678901",
            active = false
        )

        every { userRepository.findByEmail(request.email) } returns Optional.of(user)

        val exception = assertThrows<IllegalArgumentException> {
            authenticateUserUseCase.execute(request)
        }

        assertEquals("Usuário inativo", exception.message)
    }

    @Test
    fun `should throw exception when password is invalid`() {
        val request = LoginRequest(
            email = "test@test.com",
            password = "wrongpassword"
        )

        val user = User(
            id = 1L,
            email = request.email,
            passwordHash = "encodedPassword",
            firstName = "João",
            lastName = "Silva",
            cpf = "12345678901",
            active = true
        )

        every { userRepository.findByEmail(request.email) } returns Optional.of(user)
        every { passwordEncoder.matches(request.password, user.passwordHash) } returns false

        val exception = assertThrows<IllegalArgumentException> {
            authenticateUserUseCase.execute(request)
        }

        assertEquals("Credenciais inválidas", exception.message)
    }
}
