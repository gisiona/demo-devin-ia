package com.gisiona.demodevinia.infrastructure.persistence.adapter

import com.gisiona.demodevinia.domain.model.User
import com.gisiona.demodevinia.domain.port.UserRepository
import com.gisiona.demodevinia.infrastructure.persistence.entity.UserEntity
import com.gisiona.demodevinia.infrastructure.persistence.repository.JpaUserRepository
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class UserRepositoryAdapter(
    private val jpaUserRepository: JpaUserRepository
) : UserRepository {

    override fun save(user: User): User {
        val entity = user.toEntity()
        val savedEntity = jpaUserRepository.save(entity)
        return savedEntity.toDomain()
    }

    override fun findById(id: UUID): User? {
        return jpaUserRepository.findById(id)
            .map { it.toDomain() }
            .orElse(null)
    }

    override fun findByEmail(email: String): User? {
        return jpaUserRepository.findByEmail(email)?.toDomain()
    }

    override fun findAll(): List<User> {
        return jpaUserRepository.findAll().map { it.toDomain() }
    }

    override fun deleteById(id: UUID): Boolean {
        return if (jpaUserRepository.existsById(id)) {
            jpaUserRepository.deleteById(id)
            true
        } else {
            false
        }
    }

    private fun User.toEntity(): UserEntity {
        return UserEntity(
            id = this.id,
            name = this.name,
            email = this.email,
            createdAt = this.createdAt,
            updatedAt = this.updatedAt
        )
    }

    private fun UserEntity.toDomain(): User {
        return User(
            id = this.id,
            name = this.name,
            email = this.email,
            createdAt = this.createdAt,
            updatedAt = this.updatedAt
        )
    }
}
