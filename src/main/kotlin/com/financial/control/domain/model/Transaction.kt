package com.financial.control.domain.model

import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

data class Transaction(
    val id: Long = 0,
    val userId: Long,
    val accountId: Long,
    val categoryId: Long? = null,
    val amount: BigDecimal,
    val type: TransactionType,
    val description: String? = null,
    val transactionDate: LocalDate,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    val tags: List<String> = emptyList()
) {
    fun isValid(): Boolean = amount > BigDecimal.ZERO && userId > 0 && accountId > 0
    
    fun isIncome(): Boolean = type == TransactionType.INCOME
    
    fun isExpense(): Boolean = type == TransactionType.EXPENSE
}

enum class TransactionType {
    INCOME, EXPENSE, TRANSFER
}
