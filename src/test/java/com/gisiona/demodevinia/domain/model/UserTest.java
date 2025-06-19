package com.gisiona.demodevinia.domain.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void shouldCreateUserWithValidData() {
        String name = "João Silva";
        String email = "joao@example.com";
        
        User user = User.create(name, email);
        
        assertNull(user.getId());
        assertEquals(name, user.getName());
        assertEquals(email.toLowerCase(), user.getEmail());
        assertNotNull(user.getCreatedAt());
        assertNotNull(user.getUpdatedAt());
        assertEquals(user.getCreatedAt(), user.getUpdatedAt());
    }

    @Test
    void shouldThrowExceptionWhenNameIsNull() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> User.create(null, "test@example.com")
        );
        assertEquals("Nome não pode ser vazio", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenNameIsEmpty() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> User.create("", "test@example.com")
        );
        assertEquals("Nome não pode ser vazio", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenNameIsTooLong() {
        String longName = "a".repeat(101);
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> User.create(longName, "test@example.com")
        );
        assertEquals("Nome não pode ter mais de 100 caracteres", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenEmailIsNull() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> User.create("João", null)
        );
        assertEquals("Email não pode ser vazio", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenEmailIsEmpty() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> User.create("João", "")
        );
        assertEquals("Email não pode ser vazio", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenEmailIsInvalid() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> User.create("João", "invalid-email")
        );
        assertEquals("Email deve ter formato válido", exception.getMessage());
    }

    @Test
    void shouldTrimNameAndEmail() {
        User user = User.create("  João Silva  ", "  joao@example.com  ");
        
        assertEquals("João Silva", user.getName());
        assertEquals("joao@example.com", user.getEmail());
    }

    @Test
    void shouldUpdateUserCorrectly() {
        LocalDateTime originalTime = LocalDateTime.now().minusHours(1);
        User originalUser = new User(1L, "João", "joao@example.com", originalTime, originalTime);
        
        User updatedUser = originalUser.update("João Silva", "joao.silva@example.com");
        
        assertEquals(1L, updatedUser.getId());
        assertEquals("João Silva", updatedUser.getName());
        assertEquals("joao.silva@example.com", updatedUser.getEmail());
        assertEquals(originalTime, updatedUser.getCreatedAt());
        assertTrue(updatedUser.getUpdatedAt().isAfter(originalTime));
    }

    @Test
    void shouldCreateUserWithId() {
        LocalDateTime now = LocalDateTime.now();
        User user = new User(1L, "João", "joao@example.com", now, now);
        User userWithNewId = user.withId(2L);
        
        assertEquals(2L, userWithNewId.getId());
        assertEquals(user.getName(), userWithNewId.getName());
        assertEquals(user.getEmail(), userWithNewId.getEmail());
        assertEquals(user.getCreatedAt(), userWithNewId.getCreatedAt());
        assertEquals(user.getUpdatedAt(), userWithNewId.getUpdatedAt());
    }

    @Test
    void shouldImplementEqualsAndHashCodeCorrectly() {
        LocalDateTime now = LocalDateTime.now();
        User user1 = new User(1L, "João", "joao@example.com", now, now);
        User user2 = new User(1L, "Maria", "joao@example.com", now, now);
        User user3 = new User(2L, "João", "maria@example.com", now, now);
        
        assertEquals(user1, user2);
        assertNotEquals(user1, user3);
        assertEquals(user1.hashCode(), user2.hashCode());
    }
}
