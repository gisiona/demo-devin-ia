package com.financial.control.domain.port

import com.financial.control.domain.model.Investment
import java.util.*

interface InvestmentRepository {
    fun save(investment: Investment): Investment
    fun findById(id: Long): Optional<Investment>
    fun findByUserId(userId: Long): List<Investment>
    fun findActiveByUserId(userId: Long): List<Investment>
    fun delete(id: Long)
}
