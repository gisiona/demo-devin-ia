package com.financial.control.infrastructure.adapter.persistence

import com.financial.control.domain.model.Category
import com.financial.control.domain.model.CategoryType
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime

@Entity
@Table(name = "categories")
data class CategoryEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val name: String,

    val description: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val type: CategoryType,

    val color: String? = null,

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
) {
    fun toDomain(): Category = Category(
        id = id,
        name = name,
        description = description,
        type = type,
        color = color,
        createdAt = createdAt
    )
    
    companion object {
        fun fromDomain(category: Category): CategoryEntity = CategoryEntity(
            id = category.id,
            name = category.name,
            description = category.description,
            type = category.type,
            color = category.color,
            createdAt = category.createdAt
        )
    }
}
