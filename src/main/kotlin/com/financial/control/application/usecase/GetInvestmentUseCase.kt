package com.financial.control.application.usecase

import com.financial.control.application.dto.InvestmentResponse
import com.financial.control.domain.model.Investment
import com.financial.control.domain.port.InvestmentRepository
import org.springframework.stereotype.Service

@Service
class GetInvestmentUseCase(
    private val investmentRepository: InvestmentRepository
) {
    fun getInvestmentsByUserId(userId: Long): List<InvestmentResponse> {
        return investmentRepository.findActiveByUserId(userId)
            .map { it.toResponse() }
    }

    fun getInvestmentById(id: Long, userId: Long): InvestmentResponse {
        val investment = investmentRepository.findById(id)
            .orElseThrow { NoSuchElementException("Investimento não encontrado") }
            
        if (investment.userId != userId) {
            throw IllegalArgumentException("Investimento não pertence ao usuário")
        }

        return investment.toResponse()
    }
    
    private fun Investment.toResponse(): InvestmentResponse = InvestmentResponse(
        id = id,
        userId = userId,
        investmentTypeId = investmentTypeId,
        name = name,
        symbol = symbol,
        quantity = quantity,
        purchasePrice = purchasePrice,
        currentPrice = currentPrice,
        currentValue = calculateCurrentValue(),
        gainLoss = calculateGainLoss(),
        purchaseDate = purchaseDate,
        createdAt = createdAt,
        updatedAt = updatedAt,
        active = active
    )
}
