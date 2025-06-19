package com.gisiona.demodevinia.domain.port

import com.gisiona.demodevinia.domain.model.User
import java.util.UUID

interface UserRepository {
    fun save(user: User): User
    fun findById(id: UUID): User?
    fun findByEmail(email: String): User?
    fun findAll(): List<User>
    fun deleteById(id: UUID): Boolean
}
