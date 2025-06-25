package com.example.demo.controllers;

import com.example.demo.dto.AuthentificationDTO;
import com.example.demo.models.User;
import com.example.demo.securite.JwtService;
import com.example.demo.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    final  private UserService userService;
    final  private AuthenticationManager authenticationManager ;
    final  private JwtService jwtService;
    public  UserController(UserService userService  ,AuthenticationManager authenticationManager,JwtService jwtService){
        this.jwtService=jwtService;
        this.userService=userService;
        this.authenticationManager=authenticationManager;
    }

    @PostMapping("auth/sign-up")
    public ResponseEntity<String> inscription( @RequestBody User user ){
        userService.signup(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("auth/activation/{code}/{userid}")
    public  ResponseEntity<String> valider(@PathVariable String code , @PathVariable UUID userid){
        if(userService.validate(code ,userid)){
            return new ResponseEntity<>("ACTIVE",HttpStatus.OK);
        }else {
            return new ResponseEntity<>("Not active",HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("auth/sign-in")
    public Map<String,String> logingIn(@RequestBody  User user){
        log.info("loging in username:{} password: {}",user.getUsername(),user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword())
        );
        if(authenticate.isAuthenticated()) {
            return jwtService.generateJwt(user.getUsername());
        }else{
            return  null;
        }
    }
    
    @PostMapping("auth/log-out")
    public void deconnexion(){
        jwtService.deconnexion();
    }
    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUserWithProjectsAndTasks(@PathVariable UUID id) {
        try {
            User user = userService.getUserWithProjectsAndTasks(id);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
