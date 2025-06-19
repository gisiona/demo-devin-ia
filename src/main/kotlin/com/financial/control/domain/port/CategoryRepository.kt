package com.financial.control.domain.port

import com.financial.control.domain.model.Category
import com.financial.control.domain.model.CategoryType
import java.util.*

interface CategoryRepository {
    fun save(category: Category): Category
    fun findById(id: Long): Optional<Category>
    fun findAll(): List<Category>
    fun findByType(type: CategoryType): List<Category>
}
