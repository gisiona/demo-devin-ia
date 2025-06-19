package com.financial.control.integration

import com.fasterxml.jackson.databind.ObjectMapper
import com.financial.control.application.dto.CreateTransactionRequest
import com.financial.control.application.dto.CreateUserRequest
import com.financial.control.application.dto.LoginRequest
import com.financial.control.domain.model.TransactionType
import org.junit.jupiter.api.BeforeEach
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
import java.math.BigDecimal
import java.time.LocalDate

@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
@Transactional
class TransactionIntegrationTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    private lateinit var authToken: String

    @BeforeEach
    fun setup() {
        val registerRequest = CreateUserRequest(
            email = "transaction@test.com",
            password = "password123",
            firstName = "João",
            lastName = "Silva",
            cpf = "22222222222"
        )

        mockMvc.perform(
            post("/api/v1/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest))
        )

        val loginRequest = LoginRequest(
            email = "transaction@test.com",
            password = "password123"
        )

        val loginResult = mockMvc.perform(
            post("/api/v1/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))
        )
        .andExpect(status().isOk)
        .andReturn()

        val loginResponse = objectMapper.readTree(loginResult.response.contentAsString)
        authToken = loginResponse.get("token").asText()
    }

    @Test
    fun `should require authentication for transaction endpoints`() {
        val request = CreateTransactionRequest(
            accountId = 1L,
            amount = BigDecimal("100.00"),
            type = TransactionType.EXPENSE,
            transactionDate = LocalDate.now()
        )

        mockMvc.perform(
            post("/api/v1/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isUnauthorized)
    }

    @Test
    fun `should return transactions for authenticated user`() {
        mockMvc.perform(
            get("/api/v1/transactions")
                .header("Authorization", "Bearer $authToken")
        )
        .andExpect(status().isOk)
        .andExpect(jsonPath("$.content").isArray)
    }

    @Test
    fun `should validate transaction request data`() {
        val invalidRequest = CreateTransactionRequest(
            accountId = 1L,
            amount = BigDecimal("-100.00"),
            type = TransactionType.EXPENSE,
            transactionDate = LocalDate.now()
        )

        mockMvc.perform(
            post("/api/v1/transactions")
                .header("Authorization", "Bearer $authToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest))
        )
        .andExpect(status().isBadRequest)
    }
}
