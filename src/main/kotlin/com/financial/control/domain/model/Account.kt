package com.financial.control.domain.model

import java.math.BigDecimal
import java.time.LocalDateTime

data class Account(
    val id: Long = 0,
    val userId: Long,
    val name: String,
    val type: AccountType,
    val balance: BigDecimal = BigDecimal.ZERO,
    val currency: String = "BRL",
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    val active: Boolean = true
) {
    fun canDebit(amount: BigDecimal): Boolean = balance >= amount
    
    fun debit(amount: BigDecimal): Account = copy(balance = balance - amount)
    
    fun credit(amount: BigDecimal): Account = copy(balance = balance + amount)
}

enum class AccountType {
    CHECKING, SAVINGS, CREDIT_CARD, INVESTMENT
}
