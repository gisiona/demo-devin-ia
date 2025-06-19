package com.financial.control.infrastructure.adapter.persistence

import com.financial.control.domain.model.User
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import org.junit.jupiter.api.Assertions.*

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryAdapterTest {

    @Autowired
    private lateinit var userJpaRepository: UserJpaRepository

    @Test
    fun `should save and find user by email`() {
        val userRepositoryAdapter = UserRepositoryAdapter(userJpaRepository)
        
        val user = User(
            email = "test@test.com",
            passwordHash = "hashedPassword",
            firstName = "João",
            lastName = "Silva",
            cpf = "12345678901"
        )

        val savedUser = userRepositoryAdapter.save(user)
        assertTrue(savedUser.id > 0)

        val foundUser = userRepositoryAdapter.findByEmail("test@test.com")
        assertTrue(foundUser.isPresent)
        assertEquals("test@test.com", foundUser.get().email)
        assertEquals("João", foundUser.get().firstName)
    }

    @Test
    fun `should check if email exists`() {
        val userRepositoryAdapter = UserRepositoryAdapter(userJpaRepository)
        
        val user = User(
            email = "exists@test.com",
            passwordHash = "hashedPassword",
            firstName = "João",
            lastName = "Silva",
            cpf = "12345678901"
        )

        userRepositoryAdapter.save(user)

        assertTrue(userRepositoryAdapter.existsByEmail("exists@test.com"))
        assertFalse(userRepositoryAdapter.existsByEmail("notexists@test.com"))
    }

    @Test
    fun `should find only active users`() {
        val userRepositoryAdapter = UserRepositoryAdapter(userJpaRepository)
        
        val activeUser = User(
            email = "active@test.com",
            passwordHash = "hashedPassword",
            firstName = "Active",
            lastName = "User",
            cpf = "11111111111",
            active = true
        )

        val inactiveUser = User(
            email = "inactive@test.com",
            passwordHash = "hashedPassword",
            firstName = "Inactive",
            lastName = "User",
            cpf = "22222222222",
            active = false
        )

        userRepositoryAdapter.save(activeUser)
        userRepositoryAdapter.save(inactiveUser)

        val activeUsers = userRepositoryAdapter.findAllActiveUsers()
        assertEquals(1, activeUsers.size)
        assertEquals("active@test.com", activeUsers[0].email)
    }
}
