package com.example.demo.models;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Types;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;


@Entity(name = "Taches")
@Data
public class Task {
    @Id
    @JdbcTypeCode(Types.VARCHAR)
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
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Project project;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private List<Notifications> notifications;

    @PrePersist
    public void assignId() {
        if (id == null) {
            id = UUID.randomUUID();
        }
    }
}
