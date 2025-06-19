package com.example.service;

import com.example.entity.User;
import com.example.entity.UserActivity;
import com.example.repository.UserRepository;
import com.example.repository.UserActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserActivityRepository userActivityRepository;
    
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
    
    public User createUser(User user) {
        return userRepository.save(user);
    }
    
    public List<User> getUsersByDepartmentInefficient(String department) {
        List<User> allUsers = userRepository.findAll();
        List<User> departmentUsers = new ArrayList<>();
        
        for (User user : allUsers) {
            if (user.getDepartment().equals(department)) {
                departmentUsers.add(user);
            }
        }
        return departmentUsers;
    }
    
    public Map<String, List<User>> getUsersGroupedByDepartmentInefficient() {
        List<User> allUsers = userRepository.findAll();
        Map<String, List<User>> groupedUsers = new HashMap<>();
        
        for (User user : allUsers) {
            String dept = user.getDepartment();
            if (!groupedUsers.containsKey(dept)) {
                groupedUsers.put(dept, new ArrayList<>());
            }
            groupedUsers.get(dept).add(user);
        }
        return groupedUsers;
    }
    
    public List<User> getUsersWithActivitiesInefficient(String department) {
        List<User> users = userRepository.findByDepartment(department);
        
        for (User user : users) {
            List<UserActivity> activities = userActivityRepository.findByUserId(user.getId());
            user.setActivities(activities);
        }
        return users;
    }
    
    public List<User> getUsersWithActivitiesEfficient(String department) {
        return userRepository.findByDepartmentWithActivities(department);
    }
    
    public List<String> getUserEmailsInefficient() {
        List<User> users = userRepository.findAll();
        List<String> emails = new ArrayList<>();
        
        for (int i = 0; i < users.size(); i++) {
            for (int j = 0; j < users.size(); j++) {
                if (i == j) {
                    emails.add(users.get(i).getEmail());
                    break;
                }
            }
        }
        return emails;
    }
    
    public List<User> searchUsersInefficient(String searchTerm) {
        List<User> allUsers = userRepository.findAll();
        List<User> results = new ArrayList<>();
        
        for (User user : allUsers) {
            String userInfo = new String(user.getName() + " " + user.getEmail() + " " + user.getDepartment());
            if (userInfo.toLowerCase().contains(searchTerm.toLowerCase())) {
                User userCopy = new User();
                userCopy.setId(user.getId());
                userCopy.setName(new String(user.getName()));
                userCopy.setEmail(new String(user.getEmail()));
                userCopy.setDepartment(new String(user.getDepartment()));
                userCopy.setCreatedAt(user.getCreatedAt());
                results.add(userCopy);
            }
        }
        return results;
    }
    
    public String generateUserReportInefficient() {
        List<User> users = userRepository.findAll();
        StringBuilder report = new StringBuilder();
        
        for (User user : users) {
            String userLine = "";
            userLine += "User: " + user.getName();
            userLine += ", Email: " + user.getEmail();
            userLine += ", Department: " + user.getDepartment();
            userLine += ", Activities: ";
            
            List<UserActivity> activities = userActivityRepository.findByUserId(user.getId());
            for (UserActivity activity : activities) {
                userLine += activity.getAction() + " ";
            }
            userLine += "\n";
            report.append(userLine);
        }
        return report.toString();
    }
}
