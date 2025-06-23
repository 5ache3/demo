package com.example.demo.repositories;

import com.example.demo.models.Jwt;
import com.example.demo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JwtRepository extends JpaRepository<Jwt, Long> {
    Optional<Jwt> findByToken (String token);
    List<Jwt> findByUserAndDesactiveAndExpire(User user, Boolean desactive, Boolean expire);

}
