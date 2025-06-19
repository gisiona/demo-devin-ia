package com.gisiona.demodevinia.application.service

import com.gisiona.demodevinia.domain.model.User
import com.gisiona.demodevinia.domain.port.UserRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class UserServiceImplTest {

    private val userRepository = mockk<UserRepository>()
    private val userService = UserServiceImpl(userRepository)

    @Test
    fun `should create user when email is unique`() {
        val name = "John Doe"
        val email = "john@example.com"
        val user = User(name = name, email = email)

        every { userRepository.findByEmail(email) } returns null
        every { userRepository.save(any()) } returns user

        val result = userService.createUser(name, email)

        assertEquals(user, result)
        verify { userRepository.findByEmail(email) }
        verify { userRepository.save(any()) }
    }

    @Test
    fun `should throw exception when email already exists`() {
        val name = "John Doe"
        val email = "john@example.com"
        val existingUser = User(name = "Jane Doe", email = email)

        every { userRepository.findByEmail(email) } returns existingUser

        assertThrows<IllegalArgumentException> {
            userService.createUser(name, email)
        }

        verify { userRepository.findByEmail(email) }
        verify(exactly = 0) { userRepository.save(any()) }
    }

    @Test
    fun `should return user when found by id`() {
        val id = UUID.randomUUID()
        val user = User(id = id, name = "John Doe", email = "john@example.com")

        every { userRepository.findById(id) } returns user

        val result = userService.getUserById(id)

        assertEquals(user, result)
        verify { userRepository.findById(id) }
    }

    @Test
    fun `should return null when user not found by id`() {
        val id = UUID.randomUUID()

        every { userRepository.findById(id) } returns null

        val result = userService.getUserById(id)

        assertNull(result)
        verify { userRepository.findById(id) }
    }

    @Test
    fun `should return all users`() {
        val users = listOf(
            User(name = "John Doe", email = "john@example.com"),
            User(name = "Jane Doe", email = "jane@example.com")
        )

        every { userRepository.findAll() } returns users

        val result = userService.getAllUsers()

        assertEquals(users, result)
        verify { userRepository.findAll() }
    }

    @Test
    fun `should update user when exists and email is unique`() {
        val id = UUID.randomUUID()
        val existingUser = User(id = id, name = "John Doe", email = "john@example.com")
        val updatedUser = existingUser.copy(name = "John Smith")

        every { userRepository.findById(id) } returns existingUser
        every { userRepository.findByEmail("john@example.com") } returns existingUser
        every { userRepository.save(any()) } returns updatedUser

        val result = userService.updateUser(id, "John Smith", null)

        assertEquals("John Smith", result?.name)
        verify { userRepository.findById(id) }
        verify { userRepository.save(any()) }
    }

    @Test
    fun `should return null when updating non-existent user`() {
        val id = UUID.randomUUID()

        every { userRepository.findById(id) } returns null

        val result = userService.updateUser(id, "John Smith", null)

        assertNull(result)
        verify { userRepository.findById(id) }
        verify(exactly = 0) { userRepository.save(any()) }
    }

    @Test
    fun `should delete user when exists`() {
        val id = UUID.randomUUID()

        every { userRepository.deleteById(id) } returns true

        val result = userService.deleteUser(id)

        assertTrue(result)
        verify { userRepository.deleteById(id) }
    }
}
