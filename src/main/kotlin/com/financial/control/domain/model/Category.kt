package com.financial.control.domain.model

import java.time.LocalDateTime

data class Category(
    val id: Long = 0,
    val name: String,
    val description: String? = null,
    val type: CategoryType,
    val color: String? = null,
    val createdAt: LocalDateTime = LocalDateTime.now()
)

enum class CategoryType {
    INCOME, EXPENSE
}
