package com.financial.control.application.usecase

import com.financial.control.application.dto.CreateTransactionRequest
import com.financial.control.application.dto.TransactionResponse
import com.financial.control.domain.model.Transaction
import com.financial.control.domain.port.AccountRepository
import com.financial.control.domain.port.TransactionRepository
import org.springframework.stereotype.Service

@Service
class CreateTransactionUseCase(
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository
) {
    fun execute(userId: Long, request: CreateTransactionRequest): TransactionResponse {
        val account = accountRepository.findById(request.accountId)
            .orElseThrow { IllegalArgumentException("Conta não encontrada") }
            
        if (account.userId != userId) {
            throw IllegalArgumentException("Conta não pertence ao usuário")
        }

        val transaction = Transaction(
            userId = userId,
            accountId = request.accountId,
            categoryId = request.categoryId,
            amount = request.amount,
            type = request.type,
            description = request.description,
            transactionDate = request.transactionDate,
            tags = request.tags
        )

        val savedTransaction = transactionRepository.save(transaction)
        return savedTransaction.toResponse()
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
