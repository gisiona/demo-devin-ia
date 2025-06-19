package com.financial.control.application.usecase

import com.financial.control.application.dto.CreateUserRequest
import com.financial.control.application.port.PasswordEncoder
import com.financial.control.domain.model.User
import com.financial.control.domain.port.UserRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows

class CreateUserUseCaseTest {

    private val userRepository = mockk<UserRepository>()
    private val passwordEncoder = mockk<PasswordEncoder>()
    private val createUserUseCase = CreateUserUseCase(userRepository, passwordEncoder)

    @Test
    fun `should create user successfully when email and cpf are unique`() {
        val request = CreateUserRequest(
            email = "test@test.com",
            password = "password123",
            firstName = "João",
            lastName = "Silva",
            cpf = "12345678901"
        )

        val expectedUser = User(
            id = 1L,
            email = request.email,
            passwordHash = "encodedPassword",
            firstName = request.firstName,
            lastName = request.lastName,
            cpf = request.cpf
        )

        every { userRepository.existsByEmail(request.email) } returns false
        every { userRepository.existsByCpf(request.cpf) } returns false
        every { passwordEncoder.encode(request.password) } returns "encodedPassword"
        every { userRepository.save(any()) } returns expectedUser

        val result = createUserUseCase.execute(request)

        assertEquals(expectedUser, result)
        verify { userRepository.existsByEmail(request.email) }
        verify { userRepository.existsByCpf(request.cpf) }
        verify { passwordEncoder.encode(request.password) }
        verify { userRepository.save(any()) }
    }

    @Test
    fun `should throw exception when email already exists`() {
        val request = CreateUserRequest(
            email = "test@test.com",
            password = "password123",
            firstName = "João",
            lastName = "Silva",
            cpf = "12345678901"
        )

        every { userRepository.existsByEmail(request.email) } returns true

        val exception = assertThrows<IllegalArgumentException> {
            createUserUseCase.execute(request)
        }

        assertEquals("Email já está em uso", exception.message)
        verify { userRepository.existsByEmail(request.email) }
        verify(exactly = 0) { userRepository.save(any()) }
    }

    @Test
    fun `should throw exception when cpf already exists`() {
        val request = CreateUserRequest(
            email = "test@test.com",
            password = "password123",
            firstName = "João",
            lastName = "Silva",
            cpf = "12345678901"
        )

        every { userRepository.existsByEmail(request.email) } returns false
        every { userRepository.existsByCpf(request.cpf) } returns true

        val exception = assertThrows<IllegalArgumentException> {
            createUserUseCase.execute(request)
        }

        assertEquals("CPF já está cadastrado", exception.message)
        verify { userRepository.existsByEmail(request.email) }
        verify { userRepository.existsByCpf(request.cpf) }
        verify(exactly = 0) { userRepository.save(any()) }
    }
}
