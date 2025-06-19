package com.financial.control.application.port

interface JwtTokenProvider {
    fun generateToken(email: String): String
    fun validateToken(token: String): Boolean
    fun getEmailFromToken(token: String): String
}
