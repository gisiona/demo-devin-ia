package com.financial.control.application.usecase

import com.financial.control.application.dto.CreateInvestmentRequest
import com.financial.control.application.dto.InvestmentResponse
import com.financial.control.domain.model.Investment
import com.financial.control.domain.port.InvestmentRepository
import org.springframework.stereotype.Service

@Service
class CreateInvestmentUseCase(
    private val investmentRepository: InvestmentRepository
) {
    fun execute(userId: Long, request: CreateInvestmentRequest): InvestmentResponse {
        val investment = Investment(
            userId = userId,
            investmentTypeId = request.investmentTypeId,
            name = request.name,
            symbol = request.symbol,
            quantity = request.quantity,
            purchasePrice = request.purchasePrice,
            purchaseDate = request.purchaseDate
        )

        val savedInvestment = investmentRepository.save(investment)
        return savedInvestment.toResponse()
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
