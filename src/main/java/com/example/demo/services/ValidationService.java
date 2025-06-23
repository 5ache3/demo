package com.example.demo.services;

import com.example.demo.models.User;
import com.example.demo.models.Validation;
import com.example.demo.repositories.ValidationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class ValidationService {

    private final ValidationRepository validationRepository;
    private final NotificationValidationService notificationValidationService;

    public ValidationService(
        ValidationRepository validationRepository,
        NotificationValidationService notificationValidationService
    ) {
        this.validationRepository = validationRepository;
        this.notificationValidationService = notificationValidationService;
    }

    @Transactional
    public void valider(User user) {
        Validation validation = new Validation();
        validation.setUser(user);

        Instant creation = Instant.now();
        Instant expiration = creation.plus(10, ChronoUnit.MINUTES);

        // Use SecureRandom for better security
        SecureRandom random = new SecureRandom();
        int codeNumber = random.nextInt(1_000_000); // 6-digit code
        String code = String.format("%06d", codeNumber);

        validation.setCode(code);
        validation.setCreation(creation);
        validation.setExpiration(expiration);

        validationRepository.save(validation);
        notificationValidationService.envoiyer(validation);
    }

    public Validation trouver(User user) {
        return validationRepository.findByUser(user);
    }
}
