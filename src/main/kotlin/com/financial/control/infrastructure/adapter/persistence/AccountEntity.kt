package com.financial.control.infrastructure.adapter.persistence

import com.financial.control.domain.model.Account
import com.financial.control.domain.model.AccountType
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "accounts")
data class AccountEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "user_id", nullable = false)
    val userId: Long,

    @Column(nullable = false)
    val name: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val type: AccountType,

    @Column(nullable = false, precision = 15, scale = 2)
    val balance: BigDecimal = BigDecimal.ZERO,

    @Column(nullable = false)
    val currency: String = "BRL",

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    val updatedAt: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    val active: Boolean = true
) {
    fun toDomain(): Account = Account(
        id = id,
        userId = userId,
        name = name,
        type = type,
        balance = balance,
        currency = currency,
        createdAt = createdAt,
        updatedAt = updatedAt,
        active = active
    )
    
    companion object {
        fun fromDomain(account: Account): AccountEntity = AccountEntity(
            id = account.id,
            userId = account.userId,
            name = account.name,
            type = account.type,
            balance = account.balance,
            currency = account.currency,
            createdAt = account.createdAt,
            updatedAt = account.updatedAt,
            active = account.active
        )
    }
}
