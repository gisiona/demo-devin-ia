package com.financial.control.domain.port

import com.financial.control.domain.model.Account
import java.util.*

interface AccountRepository {
    fun save(account: Account): Account
    fun findById(id: Long): Optional<Account>
    fun findByUserId(userId: Long): List<Account>
    fun findActiveByUserId(userId: Long): List<Account>
    fun delete(id: Long)
}
