package com.financial.control.application.dto

import com.financial.control.domain.model.TransactionType
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

data class CreateTransactionRequest(
    @field:NotNull(message = "ID da conta é obrigatório")
    val accountId: Long,

    val categoryId: Long? = null,

    @field:NotNull(message = "Valor é obrigatório")
    @field:Positive(message = "Valor deve ser positivo")
    val amount: BigDecimal,

    @field:NotNull(message = "Tipo da transação é obrigatório")
    val type: TransactionType,

    val description: String? = null,

    @field:NotNull(message = "Data da transação é obrigatória")
    val transactionDate: LocalDate,

    val tags: List<String> = emptyList()
)

data class UpdateTransactionRequest(
    val categoryId: Long? = null,
    val description: String? = null,
    val tags: List<String> = emptyList()
)

data class TransactionResponse(
    val id: Long,
    val userId: Long,
    val accountId: Long,
    val categoryId: Long?,
    val amount: BigDecimal,
    val type: TransactionType,
    val description: String?,
    val transactionDate: LocalDate,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val tags: List<String>
)
