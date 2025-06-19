package com.financial.control.domain.model

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import java.math.BigDecimal

class AccountTest {

    @Test
    fun `should allow debit when balance is sufficient`() {
        val account = Account(
            userId = 1L,
            name = "Test Account",
            type = AccountType.CHECKING,
            balance = BigDecimal("1000.00")
        )
        
        assertTrue(account.canDebit(BigDecimal("500.00")))
    }

    @Test
    fun `should not allow debit when balance is insufficient`() {
        val account = Account(
            userId = 1L,
            name = "Test Account",
            type = AccountType.CHECKING,
            balance = BigDecimal("100.00")
        )
        
        assertFalse(account.canDebit(BigDecimal("500.00")))
    }

    @Test
    fun `should debit amount correctly`() {
        val account = Account(
            userId = 1L,
            name = "Test Account",
            type = AccountType.CHECKING,
            balance = BigDecimal("1000.00")
        )
        
        val updatedAccount = account.debit(BigDecimal("300.00"))
        assertEquals(BigDecimal("700.00"), updatedAccount.balance)
    }

    @Test
    fun `should credit amount correctly`() {
        val account = Account(
            userId = 1L,
            name = "Test Account",
            type = AccountType.CHECKING,
            balance = BigDecimal("1000.00")
        )
        
        val updatedAccount = account.credit(BigDecimal("500.00"))
        assertEquals(BigDecimal("1500.00"), updatedAccount.balance)
    }
}
