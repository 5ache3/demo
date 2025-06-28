package com.example.demo.repositories;

import com.example.demo.models.Notifications;
import com.example.demo.models.Project;
import com.example.demo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface NotificationsRepository extends JpaRepository<Notifications, UUID> {
    List<Notifications> findByDestinataire(User destinateire);
    List<Notifications> findByDestinataireAndLueFalse(User destinataire);
    List<Notifications> findByProjectAndDestinataire(Project projet,User destinateur);
    List<Notifications> findByProjectIdAndDestinataireId(String projectId,String destinataireId);
    List<Notifications> findByProjectId(String projectId);
    // List<Notifications> findByProjectIdAndDestinataireId(UUID project_id,UUID destinateur);
    Notifications findTopByDestinataireOrderByDateEnvoiDesc(User destinataire);
    void deleteAllByDestinataire(User destinataire);


}


