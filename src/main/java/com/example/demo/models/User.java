package com.example.demo.models;
import jakarta.persistence.*;
import lombok.Data;

import org.hibernate.annotations.JdbcTypeCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.UUID;
import java.sql.Types;
import java.util.Collection;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity(name ="User")
@Data
public class User implements UserDetails {

    @Id
    @JdbcTypeCode(Types.VARCHAR)
    private UUID id;

    private String name;

    @Column(unique = true) private String username;
    @Column(unique = true) private String email;

    private String imageUrl;
    private String password;
    private String role;
    private boolean actif = true;

    /* ───────────────────────────
       Collections — LAZY + @JsonIgnore
    ─────────────────────────── */

    @OneToMany(mappedBy = "user",
               cascade = CascadeType.ALL,
               fetch   = FetchType.LAZY)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private List<UserProject> projects;

    @OneToMany(mappedBy = "user",
               cascade = CascadeType.ALL,
               fetch   = FetchType.LAZY)
    @JsonIgnore
    private List<UserTask> tasks;

    @OneToMany(mappedBy = "owner",
               cascade = CascadeType.ALL,
               fetch   = FetchType.LAZY)
    @JsonIgnore
    private List<Project> ownedProjects;

    @OneToMany(mappedBy = "destinataire",
               cascade = CascadeType.ALL,
               fetch   = FetchType.LAZY)
    @JsonIgnore
    private List<Notifications> receivedNotifications;

    @OneToMany(mappedBy = "proprietaire",
               cascade = CascadeType.ALL,
               fetch   = FetchType.LAZY)
    @JsonIgnore
    private List<Notifications> sentNotifications;

    /* ───────────────────────────
       Utility
    ─────────────────────────── */
    @PrePersist
    public void assignId() {
        if (id == null) id = UUID.randomUUID();
    }

    /* ───────────────────────────
       UserDetails implementation
    ─────────────────────────── */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return java.util.List.of();               // or derive from `role`
    }
    @Override public String  getPassword()               { return password; }
    @Override public String  getUsername()               { return username; }
    @Override public boolean isAccountNonExpired()       { return true; }
    @Override public boolean isAccountNonLocked()        { return true; }
    @Override public boolean isCredentialsNonExpired()   { return true; }
    @Override public boolean isEnabled()                 { return actif; }
}
