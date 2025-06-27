package com.example.demo.models;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Types;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;

@Entity
@Data
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "task_id"}))
public class UserTask {
    @Id
    @JdbcTypeCode(Types.VARCHAR)
    private UUID id;

    private String user_id;
    private String task_id;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "task_id", insertable = false, updatable = false)
    private Task task;

    @PrePersist
    public void assignId() {
        if (id == null) {
            id = UUID.randomUUID();
        }
    }
}
