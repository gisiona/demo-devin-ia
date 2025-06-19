package com.financial.control.infrastructure.adapter.security

import com.financial.control.application.port.PasswordEncoder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component

@Component
class PasswordEncoderAdapter : PasswordEncoder {
    private val encoder = BCryptPasswordEncoder()
    
    override fun encode(rawPassword: String): String {
        return encoder.encode(rawPassword)
    }

    override fun matches(rawPassword: String, encodedPassword: String): Boolean {
        return encoder.matches(rawPassword, encodedPassword)
    }
}
