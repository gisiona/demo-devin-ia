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
@RequestMapping("/api/v1/investments")
@Tag(name = "Investment Management", description = "APIs para gerenciamento de investimentos")
@CrossOrigin(origins = ["*"])
class InvestmentController(
    private val createInvestmentUseCase: CreateInvestmentUseCase,
    private val getInvestmentUseCase: GetInvestmentUseCase,
    private val getUserUseCase: GetUserUseCase
) {

    @PostMapping
    @Operation(summary = "Criar novo investimento")
    @ApiResponses(
        ApiResponse(responseCode = "201", description = "Investimento criado com sucesso"),
        ApiResponse(responseCode = "400", description = "Dados inválidos"),
        ApiResponse(responseCode = "401", description = "Não autorizado")
    )
    fun createInvestment(
        @Valid @RequestBody request: CreateInvestmentRequest,
        authentication: Authentication
    ): ResponseEntity<InvestmentResponse> {
        val user = getUserUseCase.getUserByEmail(authentication.name)
        val investment = createInvestmentUseCase.execute(user.id, request)
        return ResponseEntity.status(HttpStatus.CREATED).body(investment)
    }

    @GetMapping
    @Operation(summary = "Listar investimentos do usuário")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Lista de investimentos obtida com sucesso"),
        ApiResponse(responseCode = "401", description = "Não autorizado")
    )
    fun getInvestments(authentication: Authentication): ResponseEntity<List<InvestmentResponse>> {
        val user = getUserUseCase.getUserByEmail(authentication.name)
        val investments = getInvestmentUseCase.getInvestmentsByUserId(user.id)
        return ResponseEntity.ok(investments)
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter investimento por ID")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Investimento encontrado"),
        ApiResponse(responseCode = "404", description = "Investimento não encontrado"),
        ApiResponse(responseCode = "401", description = "Não autorizado")
    )
    fun getInvestmentById(
        @PathVariable id: Long,
        authentication: Authentication
    ): ResponseEntity<InvestmentResponse> {
        val user = getUserUseCase.getUserByEmail(authentication.name)
        val investment = getInvestmentUseCase.getInvestmentById(id, user.id)
        return ResponseEntity.ok(investment)
    }
}
