package com.financial.control.infrastructure.adapter.persistence

import com.financial.control.domain.model.Investment
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "investments")
data class InvestmentEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "user_id", nullable = false)
    val userId: Long,

    @Column(name = "investment_type_id", nullable = false)
    val investmentTypeId: Long,

    @Column(nullable = false)
    val name: String,

    val symbol: String? = null,

    @Column(precision = 15, scale = 6)
    val quantity: BigDecimal? = null,

    @Column(name = "purchase_price", precision = 15, scale = 2)
    val purchasePrice: BigDecimal? = null,

    @Column(name = "current_price", precision = 15, scale = 2)
    val currentPrice: BigDecimal? = null,

    @Column(name = "purchase_date")
    val purchaseDate: LocalDate? = null,

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    val updatedAt: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    val active: Boolean = true
) {
    fun toDomain(): Investment = Investment(
        id = id,
        userId = userId,
        investmentTypeId = investmentTypeId,
        name = name,
        symbol = symbol,
        quantity = quantity,
        purchasePrice = purchasePrice,
        currentPrice = currentPrice,
        purchaseDate = purchaseDate,
        createdAt = createdAt,
        updatedAt = updatedAt,
        active = active
    )
    
    companion object {
        fun fromDomain(investment: Investment): InvestmentEntity = InvestmentEntity(
            id = investment.id,
            userId = investment.userId,
            investmentTypeId = investment.investmentTypeId,
            name = investment.name,
            symbol = investment.symbol,
            quantity = investment.quantity,
            purchasePrice = investment.purchasePrice,
            currentPrice = investment.currentPrice,
            purchaseDate = investment.purchaseDate,
            createdAt = investment.createdAt,
            updatedAt = investment.updatedAt,
            active = investment.active
        )
    }
}
