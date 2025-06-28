package com.example.demo.models;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Types;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity(name = "Project")
@Data
public class Project {

    @Id
    @JdbcTypeCode(Types.VARCHAR)
    private UUID id;

    private String title;

    /* Better: store ownerId as UUID and/or
       use only the @ManyToOne association */
    @Column(name = "ownerId")
    private String ownerId;

    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(columnDefinition = "TEXT")
    private String description;

    private LocalDateTime deadline;
    private int t;
    private int c;
    /*─────────────────────────────
      1. Tasks
    ─────────────────────────────*/
    @OneToMany(mappedBy = "project",
               cascade = CascadeType.ALL,
               fetch   = FetchType.LAZY)
    private List<Task> tasks;

    /*─────────────────────────────
      2. User-Project links
    ─────────────────────────────*/
    @OneToMany(mappedBy = "project",
               fetch   = FetchType.LAZY,cascade = {CascadeType.MERGE, CascadeType.REFRESH}, orphanRemoval = false)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private List<UserProject> users;

    /*─────────────────────────────
      3. Notifications
    ─────────────────────────────*/
    @OneToMany(mappedBy = "project",  // fixed: should match Notifications.java
               cascade = CascadeType.ALL,
               fetch   = FetchType.LAZY)
    @JsonIgnore
    private List<Notifications> notifications;
    
    /* Owner relation (optional) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ownerId", insertable = false, updatable = false)
    
    @JsonIgnore
    private User owner;

    @PrePersist
    public void assignId() {
        if (id == null) id = UUID.randomUUID();
    }
}
