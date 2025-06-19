package com.financial.control.domain.port

import com.financial.control.domain.model.Transaction
import com.financial.control.domain.model.TransactionType
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.time.LocalDate
import java.util.*

interface TransactionRepository {
    fun save(transaction: Transaction): Transaction
    fun findById(id: Long): Optional<Transaction>
    fun findByUserId(userId: Long, pageable: Pageable): Page<Transaction>
    fun findByUserIdAndDateRange(
        userId: Long, 
        startDate: LocalDate, 
        endDate: LocalDate, 
        pageable: Pageable
    ): Page<Transaction>
    fun findByUserIdAndType(userId: Long, type: TransactionType, pageable: Pageable): Page<Transaction>
    fun findByUserIdAndCategoryId(userId: Long, categoryId: Long, pageable: Pageable): Page<Transaction>
    fun delete(id: Long)
}
