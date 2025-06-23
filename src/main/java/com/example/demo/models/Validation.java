package com.example.demo.models;
import  jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name ="validation")

public class Validation {
    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private  Long id ;
    private Instant creation;
    private Instant expiration;
    private  Instant activation ;
    private  String code ;
    @OneToOne(cascade=CascadeType.ALL)
    private  User user;

    public void setId(Long id) {
        this.id = id;
    }
    public void setCreation(Instant creation) {
        this.creation = creation;
    }

    public void setExpiration(Instant expiration) {
        this.expiration = expiration;
    }

    public void setActivation(Instant activation) {
        this.activation = activation;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setUser(User users) {
        this.user = users;
    }

    public Long getId() {
        return id;
    }

    public Instant getCreation() {
        return creation;
    }

    public Instant getExpiration() {
        return expiration;
    }

    public Instant getActivation() {
        return activation;
    }

    public String getCode() {
        return code;
    }

    public User getUser() {
        return user;
    }
}
