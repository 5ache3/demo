package com.example.demo.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name ="Notifications")
@Data
public class Notifications {
    @Id
    private UUID id;

    private String title;
    private String message;
    private boolean lue = false;
    private LocalDateTime dateEnvoi = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType = NotificationType.GENERAL;

    @Enumerated(EnumType.STRING)
    private RequestStatus requestStatus;

    private String destinataireId;
    private String proprietaireId;

    @Column(name = "Taches_id")
    private String tacheId;

    @Column(name = "Projets_id")
    private String projetId;

    @ManyToOne
    @JoinColumn(name = "Projets_id", insertable = false, updatable = false)
    private Project projet;

    @ManyToOne
    @JoinColumn(name = "destinataireId", insertable = false, updatable = false)
    private User destinataire;

    @ManyToOne
    @JoinColumn(name = "proprietaireId", insertable = false, updatable = false)
    private User proprietaire;

    @ManyToOne
    @JoinColumn(name = "Taches_id", insertable = false, updatable = false)
    private Task task;
}

