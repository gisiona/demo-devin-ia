package com.financial.control.infrastructure.adapter.persistence

import com.financial.control.domain.model.Account
import com.financial.control.domain.port.AccountRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class AccountRepositoryAdapter(
    private val jpaRepository: AccountJpaRepository
) : AccountRepository {
    
    override fun save(account: Account): Account {
        val entity = AccountEntity.fromDomain(account)
        val savedEntity = jpaRepository.save(entity)
        return savedEntity.toDomain()
    }

    override fun findById(id: Long): Optional<Account> {
        return jpaRepository.findById(id).map { it.toDomain() }
    }

    override fun findByUserId(userId: Long): List<Account> {
        return jpaRepository.findByUserId(userId).map { it.toDomain() }
    }

    override fun findActiveByUserId(userId: Long): List<Account> {
        return jpaRepository.findActiveByUserId(userId).map { it.toDomain() }
    }

    override fun delete(id: Long) {
        jpaRepository.deleteById(id)
    }
}
