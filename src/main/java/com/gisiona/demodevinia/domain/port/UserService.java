package com.gisiona.demodevinia.domain.port;

import com.gisiona.demodevinia.domain.model.User;
import java.util.List;

public interface UserService {
    
    User createUser(String name, String email);
    
    User getUserById(Long id);
    
    User getUserByEmail(String email);
    
    List<User> getAllUsers();
    
    User updateUser(Long id, String name, String email);
    
    void deleteUser(Long id);
}
