package com.example.demo.services;

import com.example.demo.models.Notifications;

import com.example.demo.models.User;
import com.example.demo.repositories.NotificationsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class NotificationsService {

    final private NotificationsRepository notificationsRepository;
    public NotificationsService(NotificationsRepository notificationsRepository){
        this.notificationsRepository=notificationsRepository;
    }
    public void envoiyerService(Notifications   notifications ){
        notificationsRepository.save(notifications);
    }
    public List<Notifications> afficherService(UUID destinataire_id){
        User destinataire = new User();
        destinataire.setId(destinataire_id);
        return notificationsRepository.findByDestinataire(destinataire);
    }
    public Notifications  dernierService(UUID destinataire_id){
        User destinataire = new User();
        destinataire.setId(destinataire_id);
        return notificationsRepository.findTopByDestinataireOrderByDateEnvoiDesc(destinataire);
    }
    public List<Notifications> afficherNonLueService(UUID user_id){
        User user=new User();
        user.setId(user_id);
       return notificationsRepository.findByDestinataireAndLueFalse(user);
    }
    public boolean lireService(UUID  notificationId){
        Optional<Notifications> notifications=notificationsRepository.findById(notificationId);
        if(notifications.isPresent()){
            notifications.get().setLue(true);
            notificationsRepository.save(notifications.get());
            return true;
        }else{
            return false;
        }

    }
    public boolean suprimerService(UUID notificationId) {
        Optional<Notifications> notifications = notificationsRepository.findById(notificationId);
        if (notifications.isPresent()) {
            notificationsRepository.deleteById(notifications.get().getId());
            return true;
        } else {
            return false;
        }

    }

     public void suprimerTousService(UUID destinataire_id){
               User user=new User();
               user.setId(destinataire_id);
               notificationsRepository.deleteAllByDestinataire(user);

     }



}
