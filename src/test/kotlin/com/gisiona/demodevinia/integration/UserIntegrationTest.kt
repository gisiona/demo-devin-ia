package com.gisiona.demodevinia.integration

import com.gisiona.demodevinia.infrastructure.web.dto.CreateUserRequest
import com.gisiona.demodevinia.infrastructure.web.dto.UpdateUserRequest
import com.gisiona.demodevinia.infrastructure.web.dto.UserResponse
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@ActiveProfiles("test")
@Transactional
class UserIntegrationTest {

    @Autowired
    private lateinit var testRestTemplate: TestRestTemplate

    @Test
    @Order(1)
    fun `should perform complete user CRUD operations`() {
        val createRequest = CreateUserRequest("John Doe", "john@example.com")

        val createResponse = testRestTemplate.postForEntity(
            "/api/users",
            createRequest,
            UserResponse::class.java
        )

        Assertions.assertEquals(HttpStatus.CREATED, createResponse.statusCode)
        Assertions.assertNotNull(createResponse.body)
        Assertions.assertEquals("John Doe", createResponse.body!!.name)
        Assertions.assertEquals("john@example.com", createResponse.body!!.email)

        val userId = createResponse.body!!.id

        val getResponse = testRestTemplate.getForEntity(
            "/api/users/$userId",
            UserResponse::class.java
        )
        Assertions.assertEquals(HttpStatus.OK, getResponse.statusCode)
        Assertions.assertEquals("John Doe", getResponse.body!!.name)

        val getAllResponse = testRestTemplate.getForEntity(
            "/api/users",
            Array<UserResponse>::class.java
        )
        Assertions.assertEquals(HttpStatus.OK, getAllResponse.statusCode)
        Assertions.assertTrue(getAllResponse.body!!.isNotEmpty())

        val updateRequest = UpdateUserRequest("John Smith", null)
        val updateResponse = testRestTemplate.exchange(
            "/api/users/$userId",
            HttpMethod.PUT,
            HttpEntity(updateRequest),
            UserResponse::class.java
        )
        Assertions.assertEquals(HttpStatus.OK, updateResponse.statusCode)
        Assertions.assertEquals("John Smith", updateResponse.body!!.name)

        val deleteResponse = testRestTemplate.exchange(
            "/api/users/$userId",
            HttpMethod.DELETE,
            null,
            Void::class.java
        )
        Assertions.assertEquals(HttpStatus.NO_CONTENT, deleteResponse.statusCode)

        val getDeletedResponse = testRestTemplate.getForEntity(
            "/api/users/$userId",
            UserResponse::class.java
        )
        Assertions.assertEquals(HttpStatus.NOT_FOUND, getDeletedResponse.statusCode)
    }

    @Test
    @Order(2)
    fun `should prevent duplicate email creation`() {
        val createRequest = CreateUserRequest("Jane Doe", "jane@example.com")

        val firstResponse = testRestTemplate.postForEntity(
            "/api/users",
            createRequest,
            UserResponse::class.java
        )
        Assertions.assertEquals(HttpStatus.CREATED, firstResponse.statusCode)

        val secondResponse = testRestTemplate.postForEntity(
            "/api/users",
            createRequest,
            String::class.java
        )
        Assertions.assertEquals(HttpStatus.CONFLICT, secondResponse.statusCode)
    }
}
