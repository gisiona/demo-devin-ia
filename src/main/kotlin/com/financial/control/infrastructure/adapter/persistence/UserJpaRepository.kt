package com.financial.control.infrastructure.adapter.persistence

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserJpaRepository : JpaRepository<UserEntity, Long> {
    fun findByEmail(email: String): Optional<UserEntity>
    fun findByCpf(cpf: String): Optional<UserEntity>
    fun existsByEmail(email: String): Boolean
    fun existsByCpf(cpf: String): Boolean
    
    @Query("SELECT u FROM UserEntity u WHERE u.active = true")
    fun findAllActiveUsers(): List<UserEntity>
    
    @Query("SELECT u FROM UserEntity u WHERE u.id = :id AND u.active = true")
    fun findActiveUserById(id: Long): Optional<UserEntity>
}
