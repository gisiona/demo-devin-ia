package com.financial.control.infrastructure.adapter.persistence

import com.financial.control.domain.model.CategoryType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CategoryJpaRepository : JpaRepository<CategoryEntity, Long> {
    fun findByType(type: CategoryType): List<CategoryEntity>
}
