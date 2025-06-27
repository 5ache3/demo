package com.example.demo.dto;

import com.example.demo.models.User;

import java.util.UUID;

public class UserBasicDTO {
    private UUID id;
    private String name;
    private String image_url;
    private String username;
    private String role;

    public UserBasicDTO(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.username = user.getUsername();
        this.image_url= user.getImageUrl();

    }

    public UUID   getId()    { return id; }
    public String getName()  { return name; }
    public String getRole()  { return role; }
    public String getUsername() { return username; }
    public String getImageUrl() { return image_url; }
    public void setRole(String role){
        this.role=role;
    }

    // Getters and setters
}
