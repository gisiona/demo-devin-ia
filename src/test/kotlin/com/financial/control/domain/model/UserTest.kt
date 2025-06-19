package com.financial.control.domain.model

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class UserTest {

    @Test
    fun `should return full name correctly`() {
        val user = User(
            email = "test@test.com",
            passwordHash = "hash",
            firstName = "João",
            lastName = "Silva",
            cpf = "12345678901"
        )
        
        assertEquals("João Silva", user.getFullName())
    }

    @Test
    fun `should validate user for update when active and email not blank`() {
        val user = User(
            email = "test@test.com",
            passwordHash = "hash",
            firstName = "João",
            lastName = "Silva",
            cpf = "12345678901",
            active = true
        )
        
        assertTrue(user.isValidForUpdate())
    }

    @Test
    fun `should not validate user for update when inactive`() {
        val user = User(
            email = "test@test.com",
            passwordHash = "hash",
            firstName = "João",
            lastName = "Silva",
            cpf = "12345678901",
            active = false
        )
        
        assertFalse(user.isValidForUpdate())
    }

    @Test
    fun `should not validate user for update when email is blank`() {
        val user = User(
            email = "",
            passwordHash = "hash",
            firstName = "João",
            lastName = "Silva",
            cpf = "12345678901",
            active = true
        )
        
        assertFalse(user.isValidForUpdate())
    }
}
