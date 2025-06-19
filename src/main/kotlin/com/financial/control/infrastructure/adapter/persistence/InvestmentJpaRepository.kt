package com.financial.control.infrastructure.adapter.persistence

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface InvestmentJpaRepository : JpaRepository<InvestmentEntity, Long> {
    fun findByUserId(userId: Long): List<InvestmentEntity>
    
    @Query("SELECT i FROM InvestmentEntity i WHERE i.userId = :userId AND i.active = true")
    fun findActiveByUserId(userId: Long): List<InvestmentEntity>
}
