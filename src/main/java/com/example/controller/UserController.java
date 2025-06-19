package com.example.controller;

import com.example.entity.User;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }
    
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }
    
    @GetMapping("/department/{department}")
    public List<User> getUsersByDepartment(@PathVariable String department) {
        return userService.getUsersByDepartmentInefficient(department);
    }
    
    @GetMapping("/grouped")
    public Map<String, List<User>> getUsersGroupedByDepartment() {
        return userService.getUsersGroupedByDepartmentInefficient();
    }
    
    @GetMapping("/department/{department}/with-activities")
    public List<User> getUsersWithActivities(@PathVariable String department) {
        return userService.getUsersWithActivitiesEfficient(department);
    }
    
    @GetMapping("/department/{department}/with-activities-inefficient")
    public List<User> getUsersWithActivitiesInefficient(@PathVariable String department) {
        return userService.getUsersWithActivitiesInefficient(department);
    }
    
    @GetMapping("/emails")
    public List<String> getUserEmails() {
        return userService.getUserEmailsInefficient();
    }
    
    @GetMapping("/search")
    public List<User> searchUsers(@RequestParam String term) {
        return userService.searchUsersInefficient(term);
    }
    
    @GetMapping("/report")
    public String generateUserReport() {
        return userService.generateUserReportInefficient();
    }
}
