package com.financial.control.application.usecase

import com.financial.control.application.dto.TransactionResponse
import com.financial.control.domain.model.Transaction
import com.financial.control.domain.model.TransactionType
import com.financial.control.domain.port.TransactionRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class GetTransactionUseCase(
    private val transactionRepository: TransactionRepository
) {
    fun getTransactionsByUserId(userId: Long, pageable: Pageable): Page<TransactionResponse> {
        return transactionRepository.findByUserId(userId, pageable)
            .map { it.toResponse() }
    }

    fun getTransactionsByDateRange(
        userId: Long,
        startDate: LocalDate,
        endDate: LocalDate,
        pageable: Pageable
    ): Page<TransactionResponse> {
        return transactionRepository.findByUserIdAndDateRange(userId, startDate, endDate, pageable)
            .map { it.toResponse() }
    }

    fun getTransactionsByType(
        userId: Long,
        type: TransactionType,
        pageable: Pageable
    ): Page<TransactionResponse> {
        return transactionRepository.findByUserIdAndType(userId, type, pageable)
            .map { it.toResponse() }
    }

    fun getTransactionsByCategory(
        userId: Long,
        categoryId: Long,
        pageable: Pageable
    ): Page<TransactionResponse> {
        return transactionRepository.findByUserIdAndCategoryId(userId, categoryId, pageable)
            .map { it.toResponse() }
    }
    
    private fun Transaction.toResponse(): TransactionResponse = TransactionResponse(
        id = id,
        userId = userId,
        accountId = accountId,
        categoryId = categoryId,
        amount = amount,
        type = type,
        description = description,
        transactionDate = transactionDate,
        createdAt = createdAt,
        updatedAt = updatedAt,
        tags = tags
    )
}
