package com.example.demo.models;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Types;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;

@Entity
@Data
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "project_id"}))
public class UserProject {

    @Id
    @JdbcTypeCode(Types.VARCHAR) // Optional if using UUID + auto mapping
    private UUID id;

    private String role;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "project_id")
    private Project project;

    @PrePersist
    public void assignId() {
        if (id == null) {
            id = UUID.randomUUID();
        }
    }

    // Optional constructor
    public UserProject(User user, Project project, String role) {
        this.user = user;
        this.project = project;
        this.role = role;
    }

    public UserProject() {}
}
