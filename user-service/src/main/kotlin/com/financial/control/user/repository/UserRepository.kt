package com.financial.control.user.repository

import com.financial.control.user.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<User, Long> {
    
    fun findByEmail(email: String): Optional<User>
    
    fun findByCpf(cpf: String): Optional<User>
    
    fun existsByEmail(email: String): Boolean
    
    fun existsByCpf(cpf: String): Boolean
    
    @Query("SELECT u FROM User u WHERE u.active = true")
    fun findAllActiveUsers(): List<User>
    
    @Query("SELECT u FROM User u WHERE u.id = :id AND u.active = true")
    fun findActiveUserById(id: Long): Optional<User>
}
