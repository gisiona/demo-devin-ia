package com.financial.control.infrastructure.adapter.web

import com.financial.control.application.dto.*
import com.financial.control.application.usecase.*
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User Management", description = "APIs para gerenciamento de usuários")
@CrossOrigin(origins = ["*"])
class UserController(
    private val createUserUseCase: CreateUserUseCase,
    private val authenticateUserUseCase: AuthenticateUserUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val updateUserUseCase: UpdateUserUseCase
) {

    @PostMapping("/register")
    @Operation(summary = "Registrar novo usuário")
    @ApiResponses(
        ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
        ApiResponse(responseCode = "400", description = "Dados inválidos"),
        ApiResponse(responseCode = "409", description = "Email ou CPF já existe")
    )
    fun register(@Valid @RequestBody request: CreateUserRequest): ResponseEntity<UserResponse> {
        val user = createUserUseCase.execute(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(user.toResponse())
    }

    @PostMapping("/login")
    @Operation(summary = "Autenticar usuário")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Login realizado com sucesso"),
        ApiResponse(responseCode = "401", description = "Credenciais inválidas")
    )
    fun login(@Valid @RequestBody request: LoginRequest): ResponseEntity<LoginResponse> {
        val response = authenticateUserUseCase.execute(request)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/profile")
    @Operation(summary = "Obter perfil do usuário autenticado")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Perfil obtido com sucesso"),
        ApiResponse(responseCode = "401", description = "Não autorizado")
    )
    fun getProfile(authentication: Authentication): ResponseEntity<UserResponse> {
        val userResponse = getUserUseCase.getUserByEmail(authentication.name)
        return ResponseEntity.ok(userResponse)
    }

    @GetMapping
    @Operation(summary = "Listar todos os usuários")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Lista de usuários obtida com sucesso"),
        ApiResponse(responseCode = "401", description = "Não autorizado")
    )
    fun getAllUsers(): ResponseEntity<List<UserResponse>> {
        val users = getUserUseCase.getAllUsers()
        return ResponseEntity.ok(users)
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter usuário por ID")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Usuário encontrado"),
        ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    )
    fun getUserById(@PathVariable id: Long): ResponseEntity<UserResponse> {
        val user = getUserUseCase.getUserById(id)
        return ResponseEntity.ok(user)
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar dados do usuário")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
        ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    )
    fun updateUser(
        @PathVariable id: Long,
        @Valid @RequestBody request: UpdateUserRequest
    ): ResponseEntity<UserResponse> {
        val user = updateUserUseCase.updateUser(id, request)
        return ResponseEntity.ok(user)
    }

    @PutMapping("/{id}/password")
    @Operation(summary = "Alterar senha do usuário")
    @ApiResponses(
        ApiResponse(responseCode = "204", description = "Senha alterada com sucesso"),
        ApiResponse(responseCode = "400", description = "Senha atual incorreta"),
        ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    )
    fun changePassword(
        @PathVariable id: Long,
        @Valid @RequestBody request: ChangePasswordRequest
    ): ResponseEntity<Void> {
        updateUserUseCase.changePassword(id, request)
        return ResponseEntity.noContent().build()
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desativar usuário")
    @ApiResponses(
        ApiResponse(responseCode = "204", description = "Usuário desativado com sucesso"),
        ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    )
    fun deactivateUser(@PathVariable id: Long): ResponseEntity<Void> {
        updateUserUseCase.deactivateUser(id)
        return ResponseEntity.noContent().build()
    }
    
    private fun com.financial.control.domain.model.User.toResponse(): UserResponse = UserResponse(
        id = id,
        email = email,
        firstName = firstName,
        lastName = lastName,
        fullName = getFullName(),
        cpf = cpf,
        phone = phone,
        createdAt = createdAt,
        updatedAt = updatedAt,
        active = active
    )
}
