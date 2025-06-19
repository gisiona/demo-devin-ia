package com.financial.control.domain.model

import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

data class Investment(
    val id: Long = 0,
    val userId: Long,
    val investmentTypeId: Long,
    val name: String,
    val symbol: String? = null,
    val quantity: BigDecimal? = null,
    val purchasePrice: BigDecimal? = null,
    val currentPrice: BigDecimal? = null,
    val purchaseDate: LocalDate? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    val active: Boolean = true
) {
    fun calculateCurrentValue(): BigDecimal? {
        return if (quantity != null && currentPrice != null) {
            quantity * currentPrice
        } else null
    }
    
    fun calculateGainLoss(): BigDecimal? {
        return if (quantity != null && purchasePrice != null && currentPrice != null) {
            quantity * (currentPrice - purchasePrice)
        } else null
    }
}
