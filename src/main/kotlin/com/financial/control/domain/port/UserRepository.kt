package com.financial.control.domain.port

import com.financial.control.domain.model.User
import java.util.*

interface UserRepository {
    fun save(user: User): User
    fun findById(id: Long): Optional<User>
    fun findByEmail(email: String): Optional<User>
    fun findByCpf(cpf: String): Optional<User>
    fun existsByEmail(email: String): Boolean
    fun existsByCpf(cpf: String): Boolean
    fun findAllActiveUsers(): List<User>
    fun findActiveUserById(id: Long): Optional<User>
}
