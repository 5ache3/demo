package com.example.demo.services;

import com.example.demo.controllers.UserController;
import com.example.demo.models.*;
import com.example.demo.models.User;
import com.example.demo.repositories.*;

import jakarta.persistence.EntityNotFoundException;
// import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
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

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    // ─────────────────────────  Auth  ─────────────────────────
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usersRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Aucun utilisateur avec l’email " + username));
    }

    // ───────────────────────  Sign‑up  ───────────────────────
    public User signup(User user) {
        log.info("username {}",user.getUsername());
        usersRepository.findByUsername(user.getUsername())
                .ifPresent(u -> { throw new IllegalArgumentException("Le nom est déjà utilisé"); });
        if(user.getEmail()!=null){
            usersRepository.findByEmail(user.getUsername())
                    .ifPresent(u -> { throw new IllegalArgumentException("L’email est déjà utilisé"); });

        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");
        User saved = usersRepository.save(user);

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
    public User getUser(UUID userId) {
        User user = usersRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        List<UserProject> userProjects = userProjectRepository.findByUser(user);
        // List<UserTask> userTasks = userTaskRepository.findByUser(user);

        // user.setProjects(userProjects);
        // user.setTasks(userTasks);

        return user;
    }
    public void activateUser(User user) {
    user.setActif(true);
    usersRepository.save(user);
}

    public Optional<User> findById(UUID id){
        return usersRepository.findById(id);
    }

}
