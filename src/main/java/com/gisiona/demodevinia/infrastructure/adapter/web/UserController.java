package com.gisiona.demodevinia.infrastructure.adapter.web;

import com.gisiona.demodevinia.domain.model.User;
import com.gisiona.demodevinia.domain.port.UserService;
import com.gisiona.demodevinia.infrastructure.adapter.web.dto.CreateUserRequest;
import com.gisiona.demodevinia.infrastructure.adapter.web.dto.UpdateUserRequest;
import com.gisiona.demodevinia.infrastructure.adapter.web.dto.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "API para gerenciamento de usuários")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @Operation(summary = "Criar usuário", description = "Cria um novo usuário no sistema")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        User user = userService.createUser(request.getName(), request.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.from(user));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuário por ID", description = "Busca um usuário pelo seu ID")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(UserResponse.from(user));
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Buscar usuário por email", description = "Busca um usuário pelo seu email")
    public ResponseEntity<UserResponse> getUserByEmail(@PathVariable String email) {
        User user = userService.getUserByEmail(email);
        return ResponseEntity.ok(UserResponse.from(user));
    }

    @GetMapping
    @Operation(summary = "Listar todos os usuários", description = "Retorna uma lista com todos os usuários")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserResponse> responses = users.stream()
                .map(UserResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar usuário", description = "Atualiza os dados de um usuário existente")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @Valid @RequestBody UpdateUserRequest request) {
        User user = userService.updateUser(id, request.getName(), request.getEmail());
        return ResponseEntity.ok(UserResponse.from(user));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar usuário", description = "Remove um usuário do sistema")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
