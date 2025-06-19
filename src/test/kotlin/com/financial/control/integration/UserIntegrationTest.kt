package com.financial.control.integration

import com.fasterxml.jackson.databind.ObjectMapper
import com.financial.control.application.dto.CreateUserRequest
import com.financial.control.application.dto.LoginRequest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
@Transactional
class UserIntegrationTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    fun `should register user successfully`() {
        val request = CreateUserRequest(
            email = "test@test.com",
            password = "password123",
            firstName = "João",
            lastName = "Silva",
            cpf = "12345678901"
        )

        mockMvc.perform(
            post("/api/v1/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isCreated)
        .andExpect(jsonPath("$.email").value("test@test.com"))
        .andExpect(jsonPath("$.firstName").value("João"))
        .andExpect(jsonPath("$.lastName").value("Silva"))
        .andExpect(jsonPath("$.cpf").value("12345678901"))
        .andExpect(jsonPath("$.active").value(true))
    }

    @Test
    fun `should return conflict when email already exists`() {
        val request = CreateUserRequest(
            email = "duplicate@test.com",
            password = "password123",
            firstName = "João",
            lastName = "Silva",
            cpf = "12345678901"
        )

        mockMvc.perform(
            post("/api/v1/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isCreated)

        mockMvc.perform(
            post("/api/v1/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request.copy(cpf = "98765432109")))
        )
        .andExpect(status().isBadRequest)
    }

    @Test
    fun `should login successfully with valid credentials`() {
        val registerRequest = CreateUserRequest(
            email = "login@test.com",
            password = "password123",
            firstName = "João",
            lastName = "Silva",
            cpf = "11111111111"
        )

        mockMvc.perform(
            post("/api/v1/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest))
        )
        .andExpect(status().isCreated)

        val loginRequest = LoginRequest(
            email = "login@test.com",
            password = "password123"
        )

        mockMvc.perform(
            post("/api/v1/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))
        )
        .andExpect(status().isOk)
        .andExpect(jsonPath("$.token").exists())
        .andExpect(jsonPath("$.type").value("Bearer"))
        .andExpect(jsonPath("$.user.email").value("login@test.com"))
    }

    @Test
    fun `should return unauthorized for invalid credentials`() {
        val loginRequest = LoginRequest(
            email = "nonexistent@test.com",
            password = "wrongpassword"
        )

        mockMvc.perform(
            post("/api/v1/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))
        )
        .andExpect(status().isBadRequest)
    }

    @Test
    fun `should return validation error for invalid email format`() {
        val request = CreateUserRequest(
            email = "invalid-email",
            password = "password123",
            firstName = "João",
            lastName = "Silva",
            cpf = "12345678901"
        )

        mockMvc.perform(
            post("/api/v1/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isBadRequest)
    }

    @Test
    fun `should return validation error for short password`() {
        val request = CreateUserRequest(
            email = "test@test.com",
            password = "123",
            firstName = "João",
            lastName = "Silva",
            cpf = "12345678901"
        )

        mockMvc.perform(
            post("/api/v1/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isBadRequest)
    }
}
