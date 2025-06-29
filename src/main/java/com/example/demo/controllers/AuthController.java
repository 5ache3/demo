package com.example.demo.controllers;

import com.example.demo.dto.AuthentificationDTO;
import com.example.demo.dto.OnBoardingDTO;
import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;
import com.example.demo.securite.JwtService;
import com.example.demo.services.UserService;
import com.example.demo.services.ValidationService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpHeaders;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;


@RestController
@RequestMapping("/")
@Data
public class AuthController {
    final  private UserService userService;
    final  private AuthenticationManager authenticationManager ;
    final  private JwtService jwtService;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ValidationService validationService;

   @PostMapping("auth/sign-up")
    public ResponseEntity<Map<String, String>> inscription(@RequestBody User user) {

        if (user.getEmail() != null && userRepository.findByEmail(user.getEmail()).isPresent()) {
            return new ResponseEntity<>(Map.of("error", "Email déjà utilisé"), HttpStatus.CONFLICT);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");
        User saved = userRepository.save(user);

        // validationService.valider(saved);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Map.of("id", saved.getId().toString()));
    }


    @PostMapping("/onboarding/{id}")
    public ResponseEntity<String> onboarding(
            @PathVariable UUID id,
            @RequestBody OnBoardingDTO dto) {

        // 1.  fetch the same user
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                          HttpStatus.NOT_FOUND, "User not found"));

        // 2.  store the onboarding fields
        user.setName(dto.getName());
        user.setImageUrl(dto.getImageUrl());
        
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
        return new ResponseEntity<>("Nom d'utilisateur déjà utilisé", HttpStatus.CONFLICT);
        }
        user.setUsername(dto.username);

        userRepository.save(user);
        validationService.valider(user);     
        user.setActif(true);
        userRepository.save(user);

        return ResponseEntity.ok("Onboarding completed");
    }

    @PostMapping("auth/sign-in")
    public ResponseEntity<Map<String, String>> signIn(@RequestBody User loginDto) {

        /* 1️⃣  Find the user by username first, then by email */
        Optional<User> userOpt = userRepository.findByUsername(loginDto.getUsername());
        if (userOpt.isEmpty()) {
            userOpt = userRepository.findByEmail(loginDto.getUsername());
        }
        if (userOpt.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Utilisateur introuvable"));
        }

        User user = userOpt.get();

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getUsername(),         
                            loginDto.getPassword()
                    )
            );
        } catch (AuthenticationException ex) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Identifiants incorrects"));
        }

        /* 3️⃣  Generate JWT using the *username* stored in DB */
        Map<String, String> tokenMap = jwtService.generateJwt(user.getUsername());
        return ResponseEntity.ok(tokenMap);
    }
    

    @PostMapping("auth/log-out")
    public void deconnexion(HttpServletResponse response) {
        ResponseCookie deleteCookie = ResponseCookie.from("auth_token", "")
                .httpOnly(true)
                .secure(false) // true in production with HTTPS
                .path("/")     // must match original Path
                .maxAge(0)     // expires the cookie
                .sameSite("Strict") // or Lax / None, must match original
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, deleteCookie.toString());
    }
}