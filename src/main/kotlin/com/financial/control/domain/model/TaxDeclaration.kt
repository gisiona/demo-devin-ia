package com.financial.control.domain.model

import java.math.BigDecimal
import java.time.LocalDateTime

data class TaxDeclaration(
    val id: Long = 0,
    val userId: Long,
    val year: Int,
    val status: TaxDeclarationStatus = TaxDeclarationStatus.DRAFT,
    val totalIncome: BigDecimal? = null,
    val totalExpenses: BigDecimal? = null,
    val totalInvestments: BigDecimal? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
) {
    fun canSubmit(): Boolean = status == TaxDeclarationStatus.DRAFT && 
                              totalIncome != null && 
                              totalExpenses != null
}

enum class TaxDeclarationStatus {
    DRAFT, SUBMITTED, APPROVED
}
