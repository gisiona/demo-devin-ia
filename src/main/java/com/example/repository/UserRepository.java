package com.example.repository;

import com.example.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByDepartment(String department);
    
    @Query("SELECT u FROM User u JOIN FETCH u.activities WHERE u.department = ?1")
    List<User> findByDepartmentWithActivities(String department);
}
