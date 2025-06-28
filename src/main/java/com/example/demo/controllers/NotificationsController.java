package com.example.demo.controllers;

import com.example.demo.dto.NotificationResponseDTO;
import com.example.demo.models.NotificationType;
import com.example.demo.models.Notifications;
import com.example.demo.models.Project;
import com.example.demo.models.User;
import com.example.demo.repositories.ProjetsRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.NotificationsService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationsController {
    final private NotificationsService notificationsService;
    final private UserRepository userRepository;
    final private ProjetsRepository projectRepository;
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

  
  @PostMapping("/{id}/read")
  public ResponseEntity<String> read(@PathVariable UUID id){
      notificationsService.lireService(id);
      return new ResponseEntity<>(HttpStatus.OK);
  }

  @PostMapping("/{id}/aprove")
  public ResponseEntity<String> aprove(@PathVariable UUID id){
      notificationsService.approve(id);
      notificationsService.lireService(id);
      return new ResponseEntity<>(HttpStatus.OK);
  }

  @PostMapping("/{id}/reject")
  public ResponseEntity<String> reject(@PathVariable UUID id){
      notificationsService.reject(id);
      notificationsService.lireService(id);

      return new ResponseEntity<>(HttpStatus.OK);
  }

  @DeleteMapping("/{id}/delete")
  public ResponseEntity<String> delete(@PathVariable UUID id){
      notificationsService.suprimerService(id);
      return new ResponseEntity<>(HttpStatus.OK);
  }

  @GetMapping("user/{id}")
  public List<Notifications> getUsersNotifications(@PathVariable UUID id){
    return notificationsService.afficherService(id);
  }

  @PostMapping("project/{p_id}")
  public List<Notifications> getProjectNotifications(@PathVariable UUID p_id,@RequestBody UUID u_id){
    log.info("user_id {} project_id : {}",p_id,u_id);
    return notificationsService.ProjetEtDestNotifications(p_id, u_id);
  }
  @GetMapping("project/{p_id}/{u_id}")
  public List<Notifications> getProjectNotificationss(@PathVariable UUID p_id,@PathVariable UUID u_id){
    log.info("user_id {} project_id : {}",p_id,u_id);
    return notificationsService.ProjetEtDestNotifications(p_id, u_id);
  }

@PostMapping("/send")
public ResponseEntity<String> sendNotification(@RequestBody NotificationResponseDTO data) {
    Notifications notif = new Notifications();
    
    notif.setDestinataireId(data.destinateur_id.toString());
    notif.setProprietaireId(data.sender_id.toString());
    notif.setProjectId(data.project_id.toString()); 
    notif.setMessage(data.message);
    notif.setTitle(data.title);
    if(data.type.equals("REQUEST")){
      notif.setNotificationType(NotificationType.REQUEST);
    }else{
      notif.setNotificationType(NotificationType.GENERAL);
    }

    notificationsService.envoiyerService(notif);
    return new ResponseEntity<>(HttpStatus.CREATED);
}




}
