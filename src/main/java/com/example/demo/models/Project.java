package com.example.demo.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Entity(name = "Projet")
@Data
public class Project {
    @Id
    private UUID id;

    private String title;

    @Column(name = "ownerId")
    private String ownerId;

    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(columnDefinition = "TEXT")
    private String description;

    private LocalDateTime deadline;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<Task> tasks;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<UserProject> users;

    @ManyToOne
    @JoinColumn(name = "ownerId", insertable = false, updatable = false)
    private User owner;

    @OneToMany(mappedBy = "projet", cascade = CascadeType.ALL)
    private List<Notifications> notifications;
}
