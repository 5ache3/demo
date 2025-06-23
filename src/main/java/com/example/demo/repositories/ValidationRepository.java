package com.example.demo.repositories;
import com.example.demo.models.User;
import com.example.demo.models.Validation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface ValidationRepository extends JpaRepository<Validation, Long> {
    Validation findByUser(User user);
 }
