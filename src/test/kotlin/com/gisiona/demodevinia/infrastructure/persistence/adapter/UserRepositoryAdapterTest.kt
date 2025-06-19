package com.gisiona.demodevinia.infrastructure.persistence.adapter

import com.gisiona.demodevinia.domain.model.User
import com.gisiona.demodevinia.infrastructure.persistence.entity.UserEntity
import com.gisiona.demodevinia.infrastructure.persistence.repository.JpaUserRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class UserRepositoryAdapterTest {

    private val jpaUserRepository = mockk<JpaUserRepository>()
    private val userRepositoryAdapter = UserRepositoryAdapter(jpaUserRepository)

    @Test
    fun `should save user and return domain object`() {
        val user = User(name = "John Doe", email = "john@example.com")
        val userEntity = UserEntity(
            id = user.id,
            name = user.name,
            email = user.email,
            createdAt = user.createdAt,
            updatedAt = user.updatedAt
        )

        every { jpaUserRepository.save(any<UserEntity>()) } returns userEntity

        val result = userRepositoryAdapter.save(user)

        assertEquals(user.name, result.name)
        assertEquals(user.email, result.email)
        verify { jpaUserRepository.save(any<UserEntity>()) }
    }

    @Test
    fun `should find user by id and return domain object`() {
        val id = UUID.randomUUID()
        val userEntity = UserEntity(id = id, name = "John Doe", email = "john@example.com")

        every { jpaUserRepository.findById(id) } returns Optional.of(userEntity)

        val result = userRepositoryAdapter.findById(id)

        assertEquals(userEntity.name, result?.name)
        assertEquals(userEntity.email, result?.email)
        verify { jpaUserRepository.findById(id) }
    }

    @Test
    fun `should return null when user not found by id`() {
        val id = UUID.randomUUID()

        every { jpaUserRepository.findById(id) } returns Optional.empty()

        val result = userRepositoryAdapter.findById(id)

        assertNull(result)
        verify { jpaUserRepository.findById(id) }
    }

    @Test
    fun `should find user by email and return domain object`() {
        val email = "john@example.com"
        val userEntity = UserEntity(name = "John Doe", email = email)

        every { jpaUserRepository.findByEmail(email) } returns userEntity

        val result = userRepositoryAdapter.findByEmail(email)

        assertEquals(userEntity.name, result?.name)
        assertEquals(userEntity.email, result?.email)
        verify { jpaUserRepository.findByEmail(email) }
    }

    @Test
    fun `should return null when user not found by email`() {
        val email = "john@example.com"

        every { jpaUserRepository.findByEmail(email) } returns null

        val result = userRepositoryAdapter.findByEmail(email)

        assertNull(result)
        verify { jpaUserRepository.findByEmail(email) }
    }

    @Test
    fun `should delete user when exists`() {
        val id = UUID.randomUUID()

        every { jpaUserRepository.existsById(id) } returns true
        every { jpaUserRepository.deleteById(id) } returns Unit

        val result = userRepositoryAdapter.deleteById(id)

        assertTrue(result)
        verify { jpaUserRepository.existsById(id) }
        verify { jpaUserRepository.deleteById(id) }
    }
}
