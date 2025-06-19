package com.gisiona.demodevinia.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class User {
    
    private final Long id;
    private final String name;
    private final String email;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public User(Long id, String name, String email, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = validateName(name);
        this.email = validateEmail(email);
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static User create(String name, String email) {
        LocalDateTime now = LocalDateTime.now();
        return new User(null, name, email, now, now);
    }

    public User update(String name, String email) {
        return new User(this.id, name, email, this.createdAt, LocalDateTime.now());
    }

    public User withId(Long id) {
        return new User(id, this.name, this.email, this.createdAt, this.updatedAt);
    }

    private String validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser vazio");
        }
        if (name.length() > 100) {
            throw new IllegalArgumentException("Nome não pode ter mais de 100 caracteres");
        }
        return name.trim();
    }

    private String validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email não pode ser vazio");
        }
        String trimmedEmail = email.trim().toLowerCase();
        if (!trimmedEmail.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new IllegalArgumentException("Email deve ter formato válido");
        }
        return trimmedEmail;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && 
               Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
