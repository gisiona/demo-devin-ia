package com.financial.control.infrastructure.adapter.persistence

import com.financial.control.domain.model.Transaction
import com.financial.control.domain.model.TransactionType
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "transactions")
data class TransactionEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "user_id", nullable = false)
    val userId: Long,

    @Column(name = "account_id", nullable = false)
    val accountId: Long,

    @Column(name = "category_id")
    val categoryId: Long? = null,

    @Column(nullable = false, precision = 15, scale = 2)
    val amount: BigDecimal,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val type: TransactionType,

    val description: String? = null,

    @Column(name = "transaction_date", nullable = false)
    val transactionDate: LocalDate,

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    val updatedAt: LocalDateTime = LocalDateTime.now(),

    @Column(columnDefinition = "JSON")
    val tags: String? = null
) {
    fun toDomain(): Transaction = Transaction(
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
        tags = tags?.split(",") ?: emptyList()
    )
    
    companion object {
        fun fromDomain(transaction: Transaction): TransactionEntity = TransactionEntity(
            id = transaction.id,
            userId = transaction.userId,
            accountId = transaction.accountId,
            categoryId = transaction.categoryId,
            amount = transaction.amount,
            type = transaction.type,
            description = transaction.description,
            transactionDate = transaction.transactionDate,
            createdAt = transaction.createdAt,
            updatedAt = transaction.updatedAt,
            tags = if (transaction.tags.isNotEmpty()) transaction.tags.joinToString(",") else null
        )
    }
}
