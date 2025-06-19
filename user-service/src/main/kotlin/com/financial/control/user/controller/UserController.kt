package com.financial.control.user.controller

import com.financial.control.user.dto.*
import com.financial.control.user.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User Management", description = "APIs para gerenciamento de usuários")
class UserController(
    private val userService: UserService
) {

    @PostMapping("/register")
    @Operation(summary = "Registrar novo usuário", description = "Cria uma nova conta de usuário")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
            ApiResponse(responseCode = "400", description = "Dados inválidos"),
            ApiResponse(responseCode = "409", description = "Email ou CPF já existem")
        ]
    )
    fun registerUser(@Valid @RequestBody request: CreateUserRequest): ResponseEntity<Any> {
        return try {
            val user = userService.createUser(request)
            ResponseEntity.status(HttpStatus.CREATED).body(user)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("error" to e.message))
        }
    }

    @PostMapping("/login")
    @Operation(summary = "Autenticar usuário", description = "Realiza login e retorna token JWT")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Login realizado com sucesso"),
            ApiResponse(responseCode = "401", description = "Credenciais inválidas")
        ]
    )
    fun loginUser(@Valid @RequestBody request: LoginRequest): ResponseEntity<Any> {
        return try {
            val loginResponse = userService.authenticateUser(request)
            ResponseEntity.ok(loginResponse)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(mapOf("error" to e.message))
        }
    }

    @GetMapping("/profile")
    @Operation(
        summary = "Obter perfil do usuário",
        description = "Retorna os dados do usuário autenticado",
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Perfil obtido com sucesso"),
            ApiResponse(responseCode = "401", description = "Token inválido ou expirado"),
            ApiResponse(responseCode = "404", description = "Usuário não encontrado")
        ]
    )
    fun getUserProfile(authentication: Authentication): ResponseEntity<UserResponse> {
        val user = userService.getUserByEmail(authentication.name)
        return ResponseEntity.ok(user)
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Obter usuário por ID",
        description = "Retorna os dados de um usuário específico",
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Usuário encontrado"),
            ApiResponse(responseCode = "401", description = "Token inválido ou expirado"),
            ApiResponse(responseCode = "404", description = "Usuário não encontrado")
        ]
    )
    fun getUserById(@PathVariable id: Long): ResponseEntity<UserResponse> {
        val user = userService.getUserById(id)
        return ResponseEntity.ok(user)
    }

    @GetMapping
    @Operation(
        summary = "Listar todos os usuários",
        description = "Retorna lista de todos os usuários ativos",
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Lista obtida com sucesso"),
            ApiResponse(responseCode = "401", description = "Token inválido ou expirado")
        ]
    )
    fun getAllUsers(): ResponseEntity<List<UserResponse>> {
        val users = userService.getAllUsers()
        return ResponseEntity.ok(users)
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Atualizar dados do usuário",
        description = "Atualiza os dados de um usuário específico",
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
            ApiResponse(responseCode = "400", description = "Dados inválidos"),
            ApiResponse(responseCode = "401", description = "Token inválido ou expirado"),
            ApiResponse(responseCode = "404", description = "Usuário não encontrado")
        ]
    )
    fun updateUser(
        @PathVariable id: Long,
        @Valid @RequestBody request: UpdateUserRequest
    ): ResponseEntity<UserResponse> {
        val user = userService.updateUser(id, request)
        return ResponseEntity.ok(user)
    }

    @PutMapping("/{id}/password")
    @Operation(
        summary = "Alterar senha do usuário",
        description = "Altera a senha de um usuário específico",
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Senha alterada com sucesso"),
            ApiResponse(responseCode = "400", description = "Senha atual incorreta"),
            ApiResponse(responseCode = "401", description = "Token inválido ou expirado"),
            ApiResponse(responseCode = "404", description = "Usuário não encontrado")
        ]
    )
    fun changePassword(
        @PathVariable id: Long,
        @Valid @RequestBody request: ChangePasswordRequest
    ): ResponseEntity<Void> {
        userService.changePassword(id, request)
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Desativar usuário",
        description = "Desativa um usuário específico",
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Usuário desativado com sucesso"),
            ApiResponse(responseCode = "401", description = "Token inválido ou expirado"),
            ApiResponse(responseCode = "404", description = "Usuário não encontrado")
        ]
    )
    fun deactivateUser(@PathVariable id: Long): ResponseEntity<Void> {
        userService.deactivateUser(id)
        return ResponseEntity.ok().build()
    }
}
