package com.financial.control.infrastructure.adapter.persistence

import com.financial.control.domain.model.Category
import com.financial.control.domain.model.CategoryType
import com.financial.control.domain.port.CategoryRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class CategoryRepositoryAdapter(
    private val jpaRepository: CategoryJpaRepository
) : CategoryRepository {
    
    override fun save(category: Category): Category {
        val entity = CategoryEntity.fromDomain(category)
        val savedEntity = jpaRepository.save(entity)
        return savedEntity.toDomain()
    }

    override fun findById(id: Long): Optional<Category> {
        return jpaRepository.findById(id).map { it.toDomain() }
    }

    override fun findAll(): List<Category> {
        return jpaRepository.findAll().map { it.toDomain() }
    }

    override fun findByType(type: CategoryType): List<Category> {
        return jpaRepository.findByType(type).map { it.toDomain() }
    }
}
