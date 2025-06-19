package com.financial.control.infrastructure.adapter.persistence

import com.financial.control.domain.model.Transaction
import com.financial.control.domain.model.TransactionType
import com.financial.control.domain.port.TransactionRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.util.*

@Repository
class TransactionRepositoryAdapter(
    private val jpaRepository: TransactionJpaRepository
) : TransactionRepository {
    
    override fun save(transaction: Transaction): Transaction {
        val entity = TransactionEntity.fromDomain(transaction)
        val savedEntity = jpaRepository.save(entity)
        return savedEntity.toDomain()
    }

    override fun findById(id: Long): Optional<Transaction> {
        return jpaRepository.findById(id).map { it.toDomain() }
    }

    override fun findByUserId(userId: Long, pageable: Pageable): Page<Transaction> {
        return jpaRepository.findByUserId(userId, pageable).map { it.toDomain() }
    }

    override fun findByUserIdAndDateRange(
        userId: Long,
        startDate: LocalDate,
        endDate: LocalDate,
        pageable: Pageable
    ): Page<Transaction> {
        return jpaRepository.findByUserIdAndDateRange(userId, startDate, endDate, pageable)
            .map { it.toDomain() }
    }

    override fun findByUserIdAndType(
        userId: Long,
        type: TransactionType,
        pageable: Pageable
    ): Page<Transaction> {
        return jpaRepository.findByUserIdAndType(userId, type, pageable).map { it.toDomain() }
    }

    override fun findByUserIdAndCategoryId(
        userId: Long,
        categoryId: Long,
        pageable: Pageable
    ): Page<Transaction> {
        return jpaRepository.findByUserIdAndCategoryId(userId, categoryId, pageable)
            .map { it.toDomain() }
    }

    override fun delete(id: Long) {
        jpaRepository.deleteById(id)
    }
}
