package com.financial.control.application.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

data class CreateInvestmentRequest(
    @field:NotNull(message = "Tipo de investimento é obrigatório")
    val investmentTypeId: Long,

    @field:NotBlank(message = "Nome do investimento é obrigatório")
    val name: String,

    val symbol: String? = null,

    @field:Positive(message = "Quantidade deve ser positiva")
    val quantity: BigDecimal? = null,

    @field:Positive(message = "Preço de compra deve ser positivo")
    val purchasePrice: BigDecimal? = null,

    val purchaseDate: LocalDate? = null
)

data class UpdateInvestmentRequest(
    val name: String? = null,
    val symbol: String? = null,
    val quantity: BigDecimal? = null,
    val currentPrice: BigDecimal? = null
)

data class InvestmentResponse(
    val id: Long,
    val userId: Long,
    val investmentTypeId: Long,
    val name: String,
    val symbol: String?,
    val quantity: BigDecimal?,
    val purchasePrice: BigDecimal?,
    val currentPrice: BigDecimal?,
    val currentValue: BigDecimal?,
    val gainLoss: BigDecimal?,
    val purchaseDate: LocalDate?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val active: Boolean
)
