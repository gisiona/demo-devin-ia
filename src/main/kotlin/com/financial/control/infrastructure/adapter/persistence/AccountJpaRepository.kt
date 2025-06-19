package com.financial.control.infrastructure.adapter.persistence

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface AccountJpaRepository : JpaRepository<AccountEntity, Long> {
    fun findByUserId(userId: Long): List<AccountEntity>
    
    @Query("SELECT a FROM AccountEntity a WHERE a.userId = :userId AND a.active = true")
    fun findActiveByUserId(userId: Long): List<AccountEntity>
}
