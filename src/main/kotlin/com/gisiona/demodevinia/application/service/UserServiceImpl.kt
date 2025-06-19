package com.gisiona.demodevinia.application.service

import com.gisiona.demodevinia.domain.model.User
import com.gisiona.demodevinia.domain.port.UserRepository
import com.gisiona.demodevinia.domain.port.UserService
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.UUID

@Service
class UserServiceImpl(
    private val userRepository: UserRepository
) : UserService {

    override fun createUser(name: String, email: String): User {
        userRepository.findByEmail(email)?.let {
            throw IllegalArgumentException("User with email $email already exists")
        }
        
        val user = User(name = name, email = email)
        return userRepository.save(user)
    }

    override fun getUserById(id: UUID): User? {
        return userRepository.findById(id)
    }

    override fun getUserByEmail(email: String): User? {
        return userRepository.findByEmail(email)
    }

    override fun getAllUsers(): List<User> {
        return userRepository.findAll()
    }

    override fun updateUser(id: UUID, name: String?, email: String?): User? {
        val existingUser = userRepository.findById(id) ?: return null
        
        email?.let { newEmail ->
            userRepository.findByEmail(newEmail)?.let { userWithEmail ->
                if (userWithEmail.id != id) {
                    throw IllegalArgumentException("Email $newEmail is already taken")
                }
            }
        }
        
        val updatedUser = existingUser.copy(
            name = name ?: existingUser.name,
            email = email ?: existingUser.email,
            updatedAt = LocalDateTime.now()
        )
        
        return userRepository.save(updatedUser)
    }

    override fun deleteUser(id: UUID): Boolean {
        return userRepository.deleteById(id)
    }
}
