package com.financial.control.domain.model

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import java.math.BigDecimal
import java.time.LocalDate

class InvestmentTest {

    @Test
    fun `should calculate current value correctly`() {
        val investment = Investment(
            userId = 1L,
            investmentTypeId = 1L,
            name = "Test Investment",
            quantity = BigDecimal("10"),
            currentPrice = BigDecimal("50.00")
        )
        
        assertEquals(BigDecimal("500.00"), investment.calculateCurrentValue())
    }

    @Test
    fun `should return null current value when quantity or price is null`() {
        val investment = Investment(
            userId = 1L,
            investmentTypeId = 1L,
            name = "Test Investment",
            quantity = null,
            currentPrice = BigDecimal("50.00")
        )
        
        assertNull(investment.calculateCurrentValue())
    }

    @Test
    fun `should calculate gain loss correctly`() {
        val investment = Investment(
            userId = 1L,
            investmentTypeId = 1L,
            name = "Test Investment",
            quantity = BigDecimal("10"),
            purchasePrice = BigDecimal("40.00"),
            currentPrice = BigDecimal("50.00")
        )
        
        assertEquals(BigDecimal("100.00"), investment.calculateGainLoss())
    }

    @Test
    fun `should calculate loss correctly`() {
        val investment = Investment(
            userId = 1L,
            investmentTypeId = 1L,
            name = "Test Investment",
            quantity = BigDecimal("10"),
            purchasePrice = BigDecimal("60.00"),
            currentPrice = BigDecimal("50.00")
        )
        
        assertEquals(BigDecimal("-100.00"), investment.calculateGainLoss())
    }
}
