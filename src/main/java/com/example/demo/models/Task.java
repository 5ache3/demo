package com.example.demo.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Entity(name = "Taches")
@Data
public class Task {
    @Id
    private UUID id;

    private String title;

    @Column(name = "projectId")
    private String projectId;

    @Column(columnDefinition = "TEXT")
    private String description;

    private LocalDateTime createdAt = LocalDateTime.now();

    private boolean completed = false;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private List<UserTask> users;

    @ManyToOne
    @JoinColumn(name = "projectId", insertable = false, updatable = false)
    private Project project;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private List<Notifications> notifications;
}
