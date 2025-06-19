package com.gisiona.demodevinia.domain.model

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class UserTest {

    @Test
    fun `should create user with valid data`() {
        val user = User(name = "John Doe", email = "john@example.com")
        
        assertEquals("John Doe", user.name)
        assertEquals("john@example.com", user.email)
        assertNotNull(user.id)
        assertNotNull(user.createdAt)
        assertNotNull(user.updatedAt)
    }

    @Test
    fun `should throw exception when name is blank`() {
        assertThrows<IllegalArgumentException> {
            User(name = "", email = "john@example.com")
        }
    }

    @Test
    fun `should throw exception when name is whitespace only`() {
        assertThrows<IllegalArgumentException> {
            User(name = "   ", email = "john@example.com")
        }
    }

    @Test
    fun `should throw exception when email is blank`() {
        assertThrows<IllegalArgumentException> {
            User(name = "John Doe", email = "")
        }
    }

    @Test
    fun `should throw exception when email is invalid`() {
        assertThrows<IllegalArgumentException> {
            User(name = "John Doe", email = "invalid-email")
        }
    }
}
