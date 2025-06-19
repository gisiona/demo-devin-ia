package com.gisiona.demodevinia.infrastructure.adapter.persistence;

import com.gisiona.demodevinia.domain.model.User;
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
class UserRepositoryAdapterTest {

    @Mock
    private UserJpaRepository userJpaRepository;

    private UserRepositoryAdapter userRepositoryAdapter;

    @BeforeEach
    void setUp() {
        userRepositoryAdapter = new UserRepositoryAdapter(userJpaRepository);
    }

    @Test
    void shouldSaveUserSuccessfully() {
        LocalDateTime now = LocalDateTime.now();
        User user = User.create("João", "joao@example.com");
        UserEntity savedEntity = new UserEntity("João", "joao@example.com", now, now);
        savedEntity.setId(1L);

        when(userJpaRepository.save(any(UserEntity.class))).thenReturn(savedEntity);

        User result = userRepositoryAdapter.save(user);

        assertEquals(1L, result.getId());
        assertEquals("João", result.getName());
        assertEquals("joao@example.com", result.getEmail());
        verify(userJpaRepository).save(any(UserEntity.class));
    }

    @Test
    void shouldFindUserByIdSuccessfully() {
        Long id = 1L;
        LocalDateTime now = LocalDateTime.now();
        UserEntity entity = new UserEntity("João", "joao@example.com", now, now);
        entity.setId(id);

        when(userJpaRepository.findById(id)).thenReturn(Optional.of(entity));

        Optional<User> result = userRepositoryAdapter.findById(id);

        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
        assertEquals("João", result.get().getName());
        assertEquals("joao@example.com", result.get().getEmail());
        verify(userJpaRepository).findById(id);
    }

    @Test
    void shouldReturnEmptyWhenUserNotFoundById() {
        Long id = 1L;

        when(userJpaRepository.findById(id)).thenReturn(Optional.empty());

        Optional<User> result = userRepositoryAdapter.findById(id);

        assertFalse(result.isPresent());
        verify(userJpaRepository).findById(id);
    }

    @Test
    void shouldFindUserByEmailSuccessfully() {
        String email = "joao@example.com";
        LocalDateTime now = LocalDateTime.now();
        UserEntity entity = new UserEntity("João", email, now, now);
        entity.setId(1L);

        when(userJpaRepository.findByEmail(email)).thenReturn(Optional.of(entity));

        Optional<User> result = userRepositoryAdapter.findByEmail(email);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        assertEquals("João", result.get().getName());
        assertEquals(email, result.get().getEmail());
        verify(userJpaRepository).findByEmail(email);
    }

    @Test
    void shouldReturnEmptyWhenUserNotFoundByEmail() {
        String email = "joao@example.com";

        when(userJpaRepository.findByEmail(email)).thenReturn(Optional.empty());

        Optional<User> result = userRepositoryAdapter.findByEmail(email);

        assertFalse(result.isPresent());
        verify(userJpaRepository).findByEmail(email);
    }

    @Test
    void shouldFindAllUsersSuccessfully() {
        LocalDateTime now = LocalDateTime.now();
        UserEntity entity1 = new UserEntity("João", "joao@example.com", now, now);
        entity1.setId(1L);
        UserEntity entity2 = new UserEntity("Maria", "maria@example.com", now, now);
        entity2.setId(2L);
        List<UserEntity> entities = Arrays.asList(entity1, entity2);

        when(userJpaRepository.findAll()).thenReturn(entities);

        List<User> result = userRepositoryAdapter.findAll();

        assertEquals(2, result.size());
        assertEquals("João", result.get(0).getName());
        assertEquals("Maria", result.get(1).getName());
        verify(userJpaRepository).findAll();
    }

    @Test
    void shouldDeleteUserById() {
        Long id = 1L;

        userRepositoryAdapter.deleteById(id);

        verify(userJpaRepository).deleteById(id);
    }

    @Test
    void shouldCheckIfUserExistsByEmail() {
        String email = "joao@example.com";

        when(userJpaRepository.existsByEmail(email)).thenReturn(true);

        boolean result = userRepositoryAdapter.existsByEmail(email);

        assertTrue(result);
        verify(userJpaRepository).existsByEmail(email);
    }

    @Test
    void shouldReturnFalseWhenUserDoesNotExistByEmail() {
        String email = "joao@example.com";

        when(userJpaRepository.existsByEmail(email)).thenReturn(false);

        boolean result = userRepositoryAdapter.existsByEmail(email);

        assertFalse(result);
        verify(userJpaRepository).existsByEmail(email);
    }
}
