package com.financial.control.application.usecase

import com.financial.control.application.dto.CreateTransactionRequest
import com.financial.control.domain.model.Account
import com.financial.control.domain.model.AccountType
import com.financial.control.domain.model.Transaction
import com.financial.control.domain.model.TransactionType
import com.financial.control.domain.port.AccountRepository
import com.financial.control.domain.port.TransactionRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

class CreateTransactionUseCaseTest {

    private val transactionRepository = mockk<TransactionRepository>()
    private val accountRepository = mockk<AccountRepository>()
    private val createTransactionUseCase = CreateTransactionUseCase(transactionRepository, accountRepository)

    @Test
    fun `should create transaction successfully when account belongs to user`() {
        val userId = 1L
        val request = CreateTransactionRequest(
            accountId = 1L,
            categoryId = 1L,
            amount = BigDecimal("100.00"),
            type = TransactionType.EXPENSE,
            description = "Test transaction",
            transactionDate = LocalDate.now()
        )

        val account = Account(
            id = 1L,
            userId = userId,
            name = "Test Account",
            type = AccountType.CHECKING
        )

        val expectedTransaction = Transaction(
            id = 1L,
            userId = userId,
            accountId = request.accountId,
            categoryId = request.categoryId,
            amount = request.amount,
            type = request.type,
            description = request.description,
            transactionDate = request.transactionDate
        )

        every { accountRepository.findById(request.accountId) } returns Optional.of(account)
        every { transactionRepository.save(any()) } returns expectedTransaction

        val result = createTransactionUseCase.execute(userId, request)

        assertEquals(expectedTransaction.id, result.id)
        assertEquals(expectedTransaction.amount, result.amount)
        assertEquals(expectedTransaction.type, result.type)
    }

    @Test
    fun `should throw exception when account not found`() {
        val userId = 1L
        val request = CreateTransactionRequest(
            accountId = 1L,
            amount = BigDecimal("100.00"),
            type = TransactionType.EXPENSE,
            transactionDate = LocalDate.now()
        )

        every { accountRepository.findById(request.accountId) } returns Optional.empty()

        val exception = assertThrows<IllegalArgumentException> {
            createTransactionUseCase.execute(userId, request)
        }

        assertEquals("Conta não encontrada", exception.message)
    }

    @Test
    fun `should throw exception when account does not belong to user`() {
        val userId = 1L
        val request = CreateTransactionRequest(
            accountId = 1L,
            amount = BigDecimal("100.00"),
            type = TransactionType.EXPENSE,
            transactionDate = LocalDate.now()
        )

        val account = Account(
            id = 1L,
            userId = 2L,
            name = "Test Account",
            type = AccountType.CHECKING
        )

        every { accountRepository.findById(request.accountId) } returns Optional.of(account)

        val exception = assertThrows<IllegalArgumentException> {
            createTransactionUseCase.execute(userId, request)
        }

        assertEquals("Conta não pertence ao usuário", exception.message)
    }
}
