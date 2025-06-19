package com.gisiona.demodevinia.infrastructure.web.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.gisiona.demodevinia.domain.model.User
import com.gisiona.demodevinia.domain.port.UserService
import com.gisiona.demodevinia.infrastructure.web.dto.CreateUserRequest
import com.gisiona.demodevinia.infrastructure.web.dto.UpdateUserRequest
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.util.UUID

@WebMvcTest(UserController::class)
class UserControllerTest {

    @TestConfiguration
    class TestConfig {
        @Bean
        @Primary
        fun userService(): UserService = mockk()
    }

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    fun `should create user successfully`() {
        val request = CreateUserRequest("John Doe", "john@example.com")
        val user = User(name = "John Doe", email = "john@example.com")

        every { userService.createUser("John Doe", "john@example.com") } returns user

        mockMvc.perform(
            post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.name").value("John Doe"))
            .andExpect(jsonPath("$.email").value("john@example.com"))
    }

    @Test
    fun `should return conflict when email already exists`() {
        val request = CreateUserRequest("John Doe", "john@example.com")

        every { userService.createUser("John Doe", "john@example.com") } throws 
            IllegalArgumentException("User with email john@example.com already exists")

        mockMvc.perform(
            post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isConflict)
    }

    @Test
    fun `should get user by id successfully`() {
        val id = UUID.randomUUID()
        val user = User(id = id, name = "John Doe", email = "john@example.com")

        every { userService.getUserById(id) } returns user

        mockMvc.perform(get("/api/users/$id"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(id.toString()))
            .andExpect(jsonPath("$.name").value("John Doe"))
            .andExpect(jsonPath("$.email").value("john@example.com"))
    }

    @Test
    fun `should return not found when user does not exist`() {
        val id = UUID.randomUUID()

        every { userService.getUserById(id) } returns null

        mockMvc.perform(get("/api/users/$id"))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `should get all users successfully`() {
        val users = listOf(
            User(name = "John Doe", email = "john@example.com"),
            User(name = "Jane Doe", email = "jane@example.com")
        )

        every { userService.getAllUsers() } returns users

        mockMvc.perform(get("/api/users"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].name").value("John Doe"))
            .andExpect(jsonPath("$[1].name").value("Jane Doe"))
    }

    @Test
    fun `should update user successfully`() {
        val id = UUID.randomUUID()
        val request = UpdateUserRequest("John Smith", null)
        val updatedUser = User(id = id, name = "John Smith", email = "john@example.com")

        every { userService.updateUser(id, "John Smith", null) } returns updatedUser

        mockMvc.perform(
            put("/api/users/$id")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("John Smith"))
    }

    @Test
    fun `should delete user successfully`() {
        val id = UUID.randomUUID()

        every { userService.deleteUser(id) } returns true

        mockMvc.perform(delete("/api/users/$id"))
            .andExpect(status().isNoContent)
    }
}
