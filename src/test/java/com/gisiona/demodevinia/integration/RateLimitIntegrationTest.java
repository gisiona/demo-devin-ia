package com.gisiona.demodevinia.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gisiona.demodevinia.infrastructure.adapter.web.dto.CreateUserRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb-ratelimit",
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "rate-limit.requests-per-minute=3",
    "rate-limit.requests-per-hour=10"
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class RateLimitIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldApplyRateLimitToCreateUserEndpoint() throws Exception {
        for (int i = 0; i < 3; i++) {
            CreateUserRequest request = new CreateUserRequest("João Silva " + i, "joao" + i + "@example.com");
            mockMvc.perform(post("/api/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());
        }

        CreateUserRequest finalRequest = new CreateUserRequest("João Silva Final", "joao.final@example.com");
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(finalRequest)))
                .andExpect(status().isTooManyRequests())
                .andExpect(jsonPath("$.status").value(429))
                .andExpect(jsonPath("$.message").value("Muitas requisições. Tente novamente mais tarde."))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void shouldApplyRateLimitToGetAllUsersEndpoint() throws Exception {
        for (int i = 0; i < 3; i++) {
            mockMvc.perform(get("/api/users"))
                    .andExpect(status().isOk());
        }

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isTooManyRequests())
                .andExpect(jsonPath("$.status").value(429))
                .andExpect(jsonPath("$.message").value("Muitas requisições. Tente novamente mais tarde."));
    }

    @Test
    void shouldApplyRateLimitToGetUserByIdEndpoint() throws Exception {
        for (int i = 0; i < 3; i++) {
            mockMvc.perform(get("/api/users/1"))
                    .andExpect(status().isBadRequest());
        }

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isTooManyRequests())
                .andExpect(jsonPath("$.status").value(429));
    }

    @Test
    void shouldIncludeRateLimitHeaders() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(header().exists("X-Rate-Limit-Remaining"));
    }

    @Test
    void shouldApplyRateLimitToUpdateUserEndpoint() throws Exception {
        CreateUserRequest updateRequest = new CreateUserRequest("João Updated", "joao.updated@example.com");

        for (int i = 0; i < 3; i++) {
            mockMvc.perform(put("/api/users/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateRequest)))
                    .andExpect(status().isBadRequest());
        }

        mockMvc.perform(put("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isTooManyRequests())
                .andExpect(jsonPath("$.status").value(429));
    }

    @Test
    void shouldApplyRateLimitToDeleteUserEndpoint() throws Exception {
        for (int i = 0; i < 3; i++) {
            mockMvc.perform(delete("/api/users/1"))
                    .andExpect(status().isBadRequest());
        }

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isTooManyRequests())
                .andExpect(jsonPath("$.status").value(429));
    }
}
