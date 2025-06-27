package com.example.demo.controllers;
import com.example.demo.dto.UserBasicDTO;
import com.example.demo.models.Project;
import com.example.demo.models.User;
import com.example.demo.services.ProjetService;
import com.example.demo.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api")
public class UserController {
    final  private UserService userService;
    final  private ProjetService projectService;
    public  UserController(UserService userService,ProjetService projectService){
        this.userService=userService;
        this.projectService=projectService;
    }

    
    @GetMapping("/user/{id}/info")
    public ResponseEntity<UserBasicDTO> getUser(@PathVariable UUID id) {
        try {
            User user=userService.getUser(id); 
            return new ResponseEntity<>(new UserBasicDTO(user), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/user/{id}/projects")
    public ResponseEntity<List<Project>> getUserProjects(@PathVariable UUID id) {
        try {
            List<Project> projects=projectService.findAllForUser(id); 
            for(Project project : projects){
                project.setT(project.getTasks().size());
            }
            return new ResponseEntity<>(projects, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
