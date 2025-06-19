package com.financial.control.infrastructure.adapter.persistence

import com.financial.control.domain.model.TransactionType
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface TransactionJpaRepository : JpaRepository<TransactionEntity, Long> {
    fun findByUserId(userId: Long, pageable: Pageable): Page<TransactionEntity>
    
    @Query("SELECT t FROM TransactionEntity t WHERE t.userId = :userId AND t.transactionDate BETWEEN :startDate AND :endDate")
    fun findByUserIdAndDateRange(
        userId: Long, 
        startDate: LocalDate, 
        endDate: LocalDate, 
        pageable: Pageable
    ): Page<TransactionEntity>
    
    fun findByUserIdAndType(userId: Long, type: TransactionType, pageable: Pageable): Page<TransactionEntity>
    
    fun findByUserIdAndCategoryId(userId: Long, categoryId: Long, pageable: Pageable): Page<TransactionEntity>
}
