package com.financial.control.infrastructure.adapter.web

import com.financial.control.application.dto.*
import com.financial.control.application.usecase.*
import com.financial.control.domain.model.TransactionType
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/api/v1/transactions")
@Tag(name = "Transaction Management", description = "APIs para gerenciamento de transações")
@CrossOrigin(origins = ["*"])
class TransactionController(
    private val createTransactionUseCase: CreateTransactionUseCase,
    private val getTransactionUseCase: GetTransactionUseCase,
    private val getUserUseCase: GetUserUseCase
) {

    @PostMapping
    @Operation(summary = "Criar nova transação")
    @ApiResponses(
        ApiResponse(responseCode = "201", description = "Transação criada com sucesso"),
        ApiResponse(responseCode = "400", description = "Dados inválidos"),
        ApiResponse(responseCode = "401", description = "Não autorizado")
    )
    fun createTransaction(
        @Valid @RequestBody request: CreateTransactionRequest,
        authentication: Authentication
    ): ResponseEntity<TransactionResponse> {
        val user = getUserUseCase.getUserByEmail(authentication.name)
        val transaction = createTransactionUseCase.execute(user.id, request)
        return ResponseEntity.status(HttpStatus.CREATED).body(transaction)
    }

    @GetMapping
    @Operation(summary = "Listar transações do usuário")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Lista de transações obtida com sucesso"),
        ApiResponse(responseCode = "401", description = "Não autorizado")
    )
    fun getTransactions(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(required = false) startDate: LocalDate?,
        @RequestParam(required = false) endDate: LocalDate?,
        @RequestParam(required = false) type: TransactionType?,
        @RequestParam(required = false) categoryId: Long?,
        authentication: Authentication
    ): ResponseEntity<Page<TransactionResponse>> {
        val user = getUserUseCase.getUserByEmail(authentication.name)
        val pageable = PageRequest.of(page, size)
        
        val transactions = when {
            startDate != null && endDate != null -> 
                getTransactionUseCase.getTransactionsByDateRange(user.id, startDate, endDate, pageable)
            type != null -> 
                getTransactionUseCase.getTransactionsByType(user.id, type, pageable)
            categoryId != null -> 
                getTransactionUseCase.getTransactionsByCategory(user.id, categoryId, pageable)
            else -> 
                getTransactionUseCase.getTransactionsByUserId(user.id, pageable)
        }
        
        return ResponseEntity.ok(transactions)
    }
}
