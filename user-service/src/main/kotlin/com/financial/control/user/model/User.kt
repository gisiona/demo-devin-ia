package com.financial.control.user.model

import jakarta.persistence.*
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(unique = true, nullable = false)
    @Email(message = "Email deve ter um formato válido")
    @NotBlank(message = "Email é obrigatório")
    val email: String,

    @Column(name = "password_hash", nullable = false)
    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 8, message = "Senha deve ter pelo menos 8 caracteres")
    var passwordHash: String,

    @Column(name = "first_name", nullable = false)
    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    val firstName: String,

    @Column(name = "last_name", nullable = false)
    @NotBlank(message = "Sobrenome é obrigatório")
    @Size(max = 100, message = "Sobrenome deve ter no máximo 100 caracteres")
    val lastName: String,

    @Column(unique = true, nullable = false)
    @NotBlank(message = "CPF é obrigatório")
    @Size(min = 11, max = 14, message = "CPF deve ter entre 11 e 14 caracteres")
    val cpf: String,

    @Column(length = 20)
    val phone: String? = null,

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    val updatedAt: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    val active: Boolean = true
) {
    fun getFullName(): String = "$firstName $lastName"
}
