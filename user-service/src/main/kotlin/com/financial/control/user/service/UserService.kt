package com.financial.control.user.service

import com.financial.control.user.dto.*
import com.financial.control.user.model.User
import com.financial.control.user.repository.UserRepository
import com.financial.control.user.security.JwtTokenProvider
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtTokenProvider: JwtTokenProvider
) {

    fun createUser(request: CreateUserRequest): UserResponse {
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

        val savedUser = userRepository.save(user)
        return mapToUserResponse(savedUser)
    }

    fun authenticateUser(request: LoginRequest): LoginResponse {
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
            user = mapToUserResponse(user)
        )
    }

    @Transactional(readOnly = true)
    fun getUserById(id: Long): UserResponse {
        val user = userRepository.findActiveUserById(id)
            .orElseThrow { NoSuchElementException("Usuário não encontrado") }
        return mapToUserResponse(user)
    }

    @Transactional(readOnly = true)
    fun getUserByEmail(email: String): UserResponse {
        val user = userRepository.findByEmail(email)
            .orElseThrow { NoSuchElementException("Usuário não encontrado") }
        return mapToUserResponse(user)
    }

    @Transactional(readOnly = true)
    fun getAllUsers(): List<UserResponse> {
        return userRepository.findAllActiveUsers()
            .map { mapToUserResponse(it) }
    }

    fun updateUser(id: Long, request: UpdateUserRequest): UserResponse {
        val user = userRepository.findActiveUserById(id)
            .orElseThrow { NoSuchElementException("Usuário não encontrado") }

        val updatedUser = user.copy(
            firstName = request.firstName,
            lastName = request.lastName,
            phone = request.phone
        )

        val savedUser = userRepository.save(updatedUser)
        return mapToUserResponse(savedUser)
    }

    fun changePassword(id: Long, request: ChangePasswordRequest) {
        val user = userRepository.findActiveUserById(id)
            .orElseThrow { NoSuchElementException("Usuário não encontrado") }

        if (!passwordEncoder.matches(request.currentPassword, user.passwordHash)) {
            throw IllegalArgumentException("Senha atual incorreta")
        }

        user.passwordHash = passwordEncoder.encode(request.newPassword)
        userRepository.save(user)
    }

    fun deactivateUser(id: Long) {
        val user = userRepository.findActiveUserById(id)
            .orElseThrow { NoSuchElementException("Usuário não encontrado") }

        val deactivatedUser = user.copy(active = false)
        userRepository.save(deactivatedUser)
    }

    private fun mapToUserResponse(user: User): UserResponse {
        return UserResponse(
            id = user.id,
            email = user.email,
            firstName = user.firstName,
            lastName = user.lastName,
            fullName = user.getFullName(),
            cpf = user.cpf,
            phone = user.phone,
            createdAt = user.createdAt,
            updatedAt = user.updatedAt,
            active = user.active
        )
    }
}
