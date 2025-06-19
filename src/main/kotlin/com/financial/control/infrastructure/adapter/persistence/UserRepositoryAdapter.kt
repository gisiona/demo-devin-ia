package com.financial.control.infrastructure.adapter.persistence

import com.financial.control.domain.model.User
import com.financial.control.domain.port.UserRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class UserRepositoryAdapter(
    private val jpaRepository: UserJpaRepository
) : UserRepository {
    
    override fun save(user: User): User {
        val entity = UserEntity.fromDomain(user)
        val savedEntity = jpaRepository.save(entity)
        return savedEntity.toDomain()
    }

    override fun findById(id: Long): Optional<User> {
        return jpaRepository.findById(id).map { it.toDomain() }
    }

    override fun findByEmail(email: String): Optional<User> {
        return jpaRepository.findByEmail(email).map { it.toDomain() }
    }

    override fun findByCpf(cpf: String): Optional<User> {
        return jpaRepository.findByCpf(cpf).map { it.toDomain() }
    }

    override fun existsByEmail(email: String): Boolean {
        return jpaRepository.existsByEmail(email)
    }

    override fun existsByCpf(cpf: String): Boolean {
        return jpaRepository.existsByCpf(cpf)
    }

    override fun findAllActiveUsers(): List<User> {
        return jpaRepository.findAllActiveUsers().map { it.toDomain() }
    }

    override fun findActiveUserById(id: Long): Optional<User> {
        return jpaRepository.findActiveUserById(id).map { it.toDomain() }
    }
}
