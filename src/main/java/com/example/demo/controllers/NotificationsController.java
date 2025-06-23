package com.example.demo.controllers;

import com.example.demo.models.Notifications;
import com.example.demo.services.NotificationsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/notifications")
public class NotificationsController {
    final private NotificationsService notificationsService;
    public NotificationsController(NotificationsService notificationsService){
      this.notificationsService=notificationsService;

  }
  @PostMapping("/envoiyer")
  public ResponseEntity<String> envoiyer(@RequestBody Notifications notifications){
      notificationsService.envoiyerService(notifications);
      return new ResponseEntity<>(HttpStatus.CREATED);
  }
  @GetMapping("/afficher")
  public List<Notifications> afficher(@RequestParam UUID destinataire_id){
      return notificationsService.afficherService(destinataire_id);
    }
  @GetMapping("/dernier")
  public Notifications dernier(@RequestParam UUID destinataire_id){
      return  notificationsService.dernierService(destinataire_id);
  }
  @PutMapping("/{notificationId}/lire")
  public ResponseEntity<String> lire(@PathVariable UUID notificationId){
      if(notificationsService.lireService(notificationId)){
          return new ResponseEntity<>(HttpStatus.OK);
      }else {
          return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
  }
  @DeleteMapping("/suprimer/{notificationId}")
  public  ResponseEntity<String> suprimer (@PathVariable UUID notificationId ){
      if(notificationsService.suprimerService(notificationId)) {
          return new ResponseEntity<>(HttpStatus.OK);
      }else {
          return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
  }
  @GetMapping("/affichernonlue")
  public  List<Notifications> afficherNonLue(@RequestParam UUID user_id){
        return notificationsService.afficherNonLueService(user_id);
  }
  @DeleteMapping("/suprimerTous")
  public  ResponseEntity<String> suprimerTous(@RequestParam UUID destinataire_id){
      notificationsService.suprimerTousService(destinataire_id);
      return new ResponseEntity<>(HttpStatus.OK);
  }



}
