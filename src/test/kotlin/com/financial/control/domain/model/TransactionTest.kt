package com.financial.control.domain.model

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import java.math.BigDecimal
import java.time.LocalDate

class TransactionTest {

    @Test
    fun `should validate transaction when amount is positive and required fields are set`() {
        val transaction = Transaction(
            userId = 1L,
            accountId = 1L,
            amount = BigDecimal("100.00"),
            type = TransactionType.EXPENSE,
            transactionDate = LocalDate.now()
        )
        
        assertTrue(transaction.isValid())
    }

    @Test
    fun `should not validate transaction when amount is zero`() {
        val transaction = Transaction(
            userId = 1L,
            accountId = 1L,
            amount = BigDecimal.ZERO,
            type = TransactionType.EXPENSE,
            transactionDate = LocalDate.now()
        )
        
        assertFalse(transaction.isValid())
    }

    @Test
    fun `should identify income transaction correctly`() {
        val transaction = Transaction(
            userId = 1L,
            accountId = 1L,
            amount = BigDecimal("100.00"),
            type = TransactionType.INCOME,
            transactionDate = LocalDate.now()
        )
        
        assertTrue(transaction.isIncome())
        assertFalse(transaction.isExpense())
    }

    @Test
    fun `should identify expense transaction correctly`() {
        val transaction = Transaction(
            userId = 1L,
            accountId = 1L,
            amount = BigDecimal("100.00"),
            type = TransactionType.EXPENSE,
            transactionDate = LocalDate.now()
        )
        
        assertTrue(transaction.isExpense())
        assertFalse(transaction.isIncome())
    }
}
