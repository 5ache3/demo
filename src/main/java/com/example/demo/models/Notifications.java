package com.example.demo.models;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Types;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;

@Entity(name ="Notifications")
@Data
public class Notifications {
    @Id
    @JdbcTypeCode(Types.VARCHAR)
    private UUID id;
    private String title;
    private String message;
    private boolean lue = false;
    private LocalDateTime dateEnvoi = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    @Enumerated(EnumType.STRING)
    private RequestStatus requestStatus=RequestStatus.PENDING;

    private String destinataireId;
    private String proprietaireId;

    @Column(name = "Taches_id")
    private String tacheId;

    @Column(name = "Project_id")
    private String projectId;

    @ManyToOne
    @JoinColumn(name = "Project_id", insertable = false, updatable = false)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Project project;

    @ManyToOne
    @JoinColumn(name = "destinataireId", insertable = false, updatable = false)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private User destinataire;

    @ManyToOne
    @JoinColumn(name = "proprietaireId", insertable = false, updatable = false)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private User proprietaire;

    @ManyToOne
    @JoinColumn(name = "Taches_id", insertable = false, updatable = false)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Task task;


    @PrePersist
    public void assignId() {
        if (id == null) id = UUID.randomUUID();
    }
}

