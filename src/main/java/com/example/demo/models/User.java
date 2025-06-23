package com.example.demo.models;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.UUID;
import java.util.Collection;


@Entity(name ="Users")
@Data
public class User implements UserDetails {
    @Id
    private UUID id;

    private String name;

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String email;

    private String imageUrl;

    private String motdepasse;

    private String role;

    private boolean actif = false;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserProject> projects;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserTask> tasks;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Project> ownedProjects;

    @OneToMany(mappedBy = "destinataire", cascade = CascadeType.ALL)
    private List<Notifications> receivedNotifications;

    @OneToMany(mappedBy = "proprietaire", cascade = CascadeType.ALL)
    private List<Notifications> sentNotifications;

    // UserDetails implementation
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return java.util.List.of(); // or use role if you want to add authorities
    }

    @Override
    public String getPassword() {
        return motdepasse;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return actif;
    }
}
