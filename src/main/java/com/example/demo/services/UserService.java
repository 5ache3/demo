package com.example.demo.services;

import com.example.demo.models.*;
import com.example.demo.models.User;
import com.example.demo.repositories.*;

import jakarta.persistence.EntityNotFoundException;
// import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService implements UserDetailsService {

    private final UserRepository usersRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ValidationService validationService;
    private final UserProjectRepository userProjectRepository;
    private final UserTaskRepository userTaskRepository;

    // ─────────────────────────  Auth  ─────────────────────────
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return usersRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Aucun utilisateur avec l’email " + email));
    }

    // ───────────────────────  Sign‑up  ───────────────────────
    public User signup(User users) {
        usersRepository.findByName(users.getName())
                .ifPresent(u -> { throw new IllegalArgumentException("Le nom est déjà utilisé"); });

        usersRepository.findByEmail(users.getEmail())
                .ifPresent(u -> { throw new IllegalArgumentException("L’email est déjà utilisé"); });

        users.setMotdepasse(passwordEncoder.encode(users.getMotdepasse()));
        users.setRole("USER");
        User saved = usersRepository.save(users);

        validationService.valider(saved);
        return saved;
    }

    // ───────────────────────  Validate  ──────────────────────
    public boolean validate(String code, UUID userId) {
        User user = usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable"));

        Validation validation = validationService.trouver(user);

        if (code.equals(validation.getCode()) && validation.getExpiration().isAfter(Instant.now())) {
            user.setActif(true);
            // save is not needed explicitly; @Transactional will flush on commit
            return true;
        }
        return false;
    }

    // ───────────────  Fetch user + projects + tasks  ───────────────
    @Transactional(readOnly = true)
    public User getUserWithProjectsAndTasks(UUID userId) {
        User user = usersRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        List<UserProject> userProjects = userProjectRepository.findByUser(user);
        List<UserTask> userTasks = userTaskRepository.findByUser(user);

        user.setProjects(userProjects);
        user.setTasks(userTasks);

        return user;
    }

}
