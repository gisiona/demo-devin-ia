package com.gisiona.demodevinia.infrastructure.adapter.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gisiona.demodevinia.domain.model.User;
import com.gisiona.demodevinia.domain.port.UserService;
import com.gisiona.demodevinia.infrastructure.adapter.web.dto.CreateUserRequest;
import com.gisiona.demodevinia.infrastructure.adapter.web.dto.UpdateUserRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateUserSuccessfully() throws Exception {
        CreateUserRequest request = new CreateUserRequest("João Silva", "joao@example.com");
        User user = new User(1L, "João Silva", "joao@example.com", LocalDateTime.now(), LocalDateTime.now());

        when(userService.createUser("João Silva", "joao@example.com")).thenReturn(user);

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("João Silva"))
                .andExpect(jsonPath("$.email").value("joao@example.com"));
    }

    @Test
    void shouldReturnBadRequestWhenCreateUserWithInvalidData() throws Exception {
        CreateUserRequest request = new CreateUserRequest("", "invalid-email");

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.name").exists())
                .andExpect(jsonPath("$.errors.email").exists());
    }

    @Test
    void shouldGetUserByIdSuccessfully() throws Exception {
        Long id = 1L;
        User user = new User(id, "João Silva", "joao@example.com", LocalDateTime.now(), LocalDateTime.now());

        when(userService.getUserById(id)).thenReturn(user);

        mockMvc.perform(get("/api/users/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("João Silva"))
                .andExpect(jsonPath("$.email").value("joao@example.com"));
    }

    @Test
    void shouldReturnNotFoundWhenUserDoesNotExist() throws Exception {
        Long id = 1L;

        when(userService.getUserById(id)).thenThrow(new IllegalArgumentException("Usuário não encontrado com ID: " + id));

        mockMvc.perform(get("/api/users/{id}", id))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Usuário não encontrado com ID: " + id));
    }

    @Test
    void shouldGetUserByEmailSuccessfully() throws Exception {
        String email = "joao@example.com";
        User user = new User(1L, "João Silva", email, LocalDateTime.now(), LocalDateTime.now());

        when(userService.getUserByEmail(email)).thenReturn(user);

        mockMvc.perform(get("/api/users/email/{email}", email))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("João Silva"))
                .andExpect(jsonPath("$.email").value(email));
    }

    @Test
    void shouldGetAllUsersSuccessfully() throws Exception {
        List<User> users = Arrays.asList(
            new User(1L, "João Silva", "joao@example.com", LocalDateTime.now(), LocalDateTime.now()),
            new User(2L, "Maria Santos", "maria@example.com", LocalDateTime.now(), LocalDateTime.now())
        );

        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("João Silva"))
                .andExpect(jsonPath("$[1].name").value("Maria Santos"));
    }

    @Test
    void shouldUpdateUserSuccessfully() throws Exception {
        Long id = 1L;
        UpdateUserRequest request = new UpdateUserRequest("João Silva Updated", "joao.updated@example.com");
        User updatedUser = new User(id, "João Silva Updated", "joao.updated@example.com", LocalDateTime.now(), LocalDateTime.now());

        when(userService.updateUser(eq(id), eq("João Silva Updated"), eq("joao.updated@example.com"))).thenReturn(updatedUser);

        mockMvc.perform(put("/api/users/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("João Silva Updated"))
                .andExpect(jsonPath("$.email").value("joao.updated@example.com"));
    }

    @Test
    void shouldReturnBadRequestWhenUpdateUserWithInvalidData() throws Exception {
        Long id = 1L;
        UpdateUserRequest request = new UpdateUserRequest("", "invalid-email");

        mockMvc.perform(put("/api/users/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.name").exists())
                .andExpect(jsonPath("$.errors.email").exists());
    }

    @Test
    void shouldDeleteUserSuccessfully() throws Exception {
        Long id = 1L;

        mockMvc.perform(delete("/api/users/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnBadRequestWhenDeletingNonExistentUser() throws Exception {
        Long id = 1L;

        doThrow(new IllegalArgumentException("Usuário não encontrado com ID: " + id))
                .when(userService).deleteUser(id);

        mockMvc.perform(delete("/api/users/{id}", id))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Usuário não encontrado com ID: " + id));
    }
}
