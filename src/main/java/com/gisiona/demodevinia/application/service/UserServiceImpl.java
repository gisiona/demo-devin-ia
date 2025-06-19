package com.gisiona.demodevinia.application.service;

import com.gisiona.demodevinia.domain.model.User;
import com.gisiona.demodevinia.domain.port.UserRepository;
import com.gisiona.demodevinia.domain.port.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(String name, String email) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email já está em uso: " + email);
        }
        
        User user = User.create(name, email);
        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado com ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado com email: " + email));
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User updateUser(Long id, String name, String email) {
        User existingUser = getUserById(id);
        
        if (!existingUser.getEmail().equals(email) && userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email já está em uso: " + email);
        }
        
        User updatedUser = existingUser.update(name, email);
        return userRepository.save(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.findById(id).isPresent()) {
            throw new IllegalArgumentException("Usuário não encontrado com ID: " + id);
        }
        userRepository.deleteById(id);
    }
}
