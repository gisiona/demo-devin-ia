package com.financial.control.infrastructure.adapter.persistence

import com.financial.control.domain.model.User
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@Entity
@Table(name = "users")
data class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(unique = true, nullable = false)
    val email: String,

    @Column(name = "password_hash", nullable = false)
    var passwordHash: String,

    @Column(name = "first_name", nullable = false)
    val firstName: String,

    @Column(name = "last_name", nullable = false)
    val lastName: String,

    @Column(unique = true, nullable = false)
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
    fun toDomain(): User = User(
        id = id,
        email = email,
        passwordHash = passwordHash,
        firstName = firstName,
        lastName = lastName,
        cpf = cpf,
        phone = phone,
        createdAt = createdAt,
        updatedAt = updatedAt,
        active = active
    )
    
    companion object {
        fun fromDomain(user: User): UserEntity = UserEntity(
            id = user.id,
            email = user.email,
            passwordHash = user.passwordHash,
            firstName = user.firstName,
            lastName = user.lastName,
            cpf = user.cpf,
            phone = user.phone,
            createdAt = user.createdAt,
            updatedAt = user.updatedAt,
            active = user.active
        )
    }
}
