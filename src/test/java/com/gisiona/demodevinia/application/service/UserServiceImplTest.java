package com.gisiona.demodevinia.application.service;

import com.gisiona.demodevinia.domain.model.User;
import com.gisiona.demodevinia.domain.port.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    void shouldCreateUserSuccessfully() {
        String name = "João Silva";
        String email = "joao@example.com";
        User savedUser = new User(1L, name, email, LocalDateTime.now(), LocalDateTime.now());

        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User result = userService.createUser(name, email);

        assertEquals(savedUser, result);
        verify(userRepository).existsByEmail(email);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        String name = "João Silva";
        String email = "joao@example.com";

        when(userRepository.existsByEmail(email)).thenReturn(true);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> userService.createUser(name, email)
        );

        assertEquals("Email já está em uso: " + email, exception.getMessage());
        verify(userRepository).existsByEmail(email);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldGetUserByIdSuccessfully() {
        Long id = 1L;
        User user = new User(id, "João", "joao@example.com", LocalDateTime.now(), LocalDateTime.now());

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        User result = userService.getUserById(id);

        assertEquals(user, result);
        verify(userRepository).findById(id);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundById() {
        Long id = 1L;

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> userService.getUserById(id)
        );

        assertEquals("Usuário não encontrado com ID: " + id, exception.getMessage());
        verify(userRepository).findById(id);
    }

    @Test
    void shouldGetUserByEmailSuccessfully() {
        String email = "joao@example.com";
        User user = new User(1L, "João", email, LocalDateTime.now(), LocalDateTime.now());

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        User result = userService.getUserByEmail(email);

        assertEquals(user, result);
        verify(userRepository).findByEmail(email);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundByEmail() {
        String email = "joao@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> userService.getUserByEmail(email)
        );

        assertEquals("Usuário não encontrado com email: " + email, exception.getMessage());
        verify(userRepository).findByEmail(email);
    }

    @Test
    void shouldGetAllUsersSuccessfully() {
        List<User> users = Arrays.asList(
            new User(1L, "João", "joao@example.com", LocalDateTime.now(), LocalDateTime.now()),
            new User(2L, "Maria", "maria@example.com", LocalDateTime.now(), LocalDateTime.now())
        );

        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();

        assertEquals(users, result);
        verify(userRepository).findAll();
    }

    @Test
    void shouldUpdateUserSuccessfully() {
        Long id = 1L;
        String newName = "João Silva";
        String newEmail = "joao.silva@example.com";
        User existingUser = new User(id, "João", "joao@example.com", LocalDateTime.now(), LocalDateTime.now());
        User updatedUser = existingUser.update(newName, newEmail);

        when(userRepository.findById(id)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByEmail(newEmail)).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        User result = userService.updateUser(id, newName, newEmail);

        assertEquals(updatedUser.getName(), result.getName());
        assertEquals(updatedUser.getEmail(), result.getEmail());
        verify(userRepository).findById(id);
        verify(userRepository).existsByEmail(newEmail);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldUpdateUserWithSameEmail() {
        Long id = 1L;
        String newName = "João Silva";
        String sameEmail = "joao@example.com";
        User existingUser = new User(id, "João", sameEmail, LocalDateTime.now(), LocalDateTime.now());
        User updatedUser = existingUser.update(newName, sameEmail);

        when(userRepository.findById(id)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        User result = userService.updateUser(id, newName, sameEmail);

        assertEquals(updatedUser.getName(), result.getName());
        assertEquals(updatedUser.getEmail(), result.getEmail());
        verify(userRepository).findById(id);
        verify(userRepository, never()).existsByEmail(sameEmail);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingWithExistingEmail() {
        Long id = 1L;
        String newName = "João Silva";
        String existingEmail = "maria@example.com";
        User existingUser = new User(id, "João", "joao@example.com", LocalDateTime.now(), LocalDateTime.now());

        when(userRepository.findById(id)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByEmail(existingEmail)).thenReturn(true);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> userService.updateUser(id, newName, existingEmail)
        );

        assertEquals("Email já está em uso: " + existingEmail, exception.getMessage());
        verify(userRepository).findById(id);
        verify(userRepository).existsByEmail(existingEmail);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldDeleteUserSuccessfully() {
        Long id = 1L;
        User user = new User(id, "João", "joao@example.com", LocalDateTime.now(), LocalDateTime.now());

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        userService.deleteUser(id);

        verify(userRepository).findById(id);
        verify(userRepository).deleteById(id);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentUser() {
        Long id = 1L;

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> userService.deleteUser(id)
        );

        assertEquals("Usuário não encontrado com ID: " + id, exception.getMessage());
        verify(userRepository).findById(id);
        verify(userRepository, never()).deleteById(id);
    }
}
