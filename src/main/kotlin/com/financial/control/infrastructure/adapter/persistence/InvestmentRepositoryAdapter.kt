package com.financial.control.infrastructure.adapter.persistence

import com.financial.control.domain.model.Investment
import com.financial.control.domain.port.InvestmentRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class InvestmentRepositoryAdapter(
    private val jpaRepository: InvestmentJpaRepository
) : InvestmentRepository {
    
    override fun save(investment: Investment): Investment {
        val entity = InvestmentEntity.fromDomain(investment)
        val savedEntity = jpaRepository.save(entity)
        return savedEntity.toDomain()
    }

    override fun findById(id: Long): Optional<Investment> {
        return jpaRepository.findById(id).map { it.toDomain() }
    }

    override fun findByUserId(userId: Long): List<Investment> {
        return jpaRepository.findByUserId(userId).map { it.toDomain() }
    }

    override fun findActiveByUserId(userId: Long): List<Investment> {
        return jpaRepository.findActiveByUserId(userId).map { it.toDomain() }
    }

    override fun delete(id: Long) {
        jpaRepository.deleteById(id)
    }
}
