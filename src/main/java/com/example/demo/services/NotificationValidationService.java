package com.example.demo.services;


import com.example.demo.models.Jwt;
import com.example.demo.models.Validation;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class NotificationValidationService {
    JavaMailSender javaMailSender;
    public  NotificationValidationService(JavaMailSender javaMailSender){
        this.javaMailSender=javaMailSender;
    }
    public void  envoiyer(@org.jetbrains.annotations.NotNull Validation validation){
        SimpleMailMessage mailMessage =new SimpleMailMessage();
        mailMessage.setFrom("mvcheikhna@gmail.com");
        mailMessage.setTo(validation.getUser().getEmail());
        mailMessage.setSubject("validation email ");
        mailMessage.setText(String.format("Bonjour %s, votre code est %s", validation.getUser().getName(), validation.getCode()));
        javaMailSender.send(mailMessage);
    }
}
