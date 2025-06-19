package com.financial.control.infrastructure.adapter.security

import com.financial.control.domain.port.UserRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceAdapter(
    private val userRepository: UserRepository
) : UserDetailsService {

    override fun loadUserByUsername(email: String): UserDetails {
        val user = userRepository.findByEmail(email)
            .orElseThrow { UsernameNotFoundException("Usuário não encontrado: $email") }

        return User.builder()
            .username(user.email)
            .password(user.passwordHash)
            .authorities("USER")
            .accountExpired(false)
            .accountLocked(!user.active)
            .credentialsExpired(false)
            .disabled(!user.active)
            .build()
    }
}
