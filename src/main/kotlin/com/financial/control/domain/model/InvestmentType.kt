package com.financial.control.domain.model

import java.time.LocalDateTime

data class InvestmentType(
    val id: Long = 0,
    val name: String,
    val description: String? = null,
    val taxCategory: String? = null,
    val createdAt: LocalDateTime = LocalDateTime.now()
)
