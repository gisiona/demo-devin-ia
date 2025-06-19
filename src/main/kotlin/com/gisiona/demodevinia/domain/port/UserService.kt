package com.gisiona.demodevinia.domain.port

import com.gisiona.demodevinia.domain.model.User
import java.util.UUID

interface UserService {
    fun createUser(name: String, email: String): User
    fun getUserById(id: UUID): User?
    fun getUserByEmail(email: String): User?
    fun getAllUsers(): List<User>
    fun updateUser(id: UUID, name: String?, email: String?): User?
    fun deleteUser(id: UUID): Boolean
}
