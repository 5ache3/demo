package com.example.demo.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Jwt {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private  Long id ;
   private  Boolean desactive ;
   private  Boolean expire;
   @Column(unique = true)
   private  String token;
   @ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE})
   @JoinColumn(name = "user_id")
   private User user ;



    public Boolean getDesactive() {
        return desactive;
    }

    public Boolean getExpire() {
        return expire;
    }

    public User getUser() {
        return user;
    }

    public void setDesactive(Boolean desactive) {
        this.desactive = desactive;
    }

    public void setExpire(Boolean expire) {
        this.expire = expire;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
