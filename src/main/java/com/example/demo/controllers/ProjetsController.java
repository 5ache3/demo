package com.example.demo.controllers;


import com.example.demo.dto.ProjectResponseDTO;
import com.example.demo.dto.UserProjectRelDTO;
import com.example.demo.dto.ProjectUpdateDTO;
import com.example.demo.models.Project;
import com.example.demo.models.Task;
import com.example.demo.models.User;
import com.example.demo.models.UserProject;
import com.example.demo.repositories.UserProjectRepository;
import com.example.demo.services.ProjetService;
import com.example.demo.services.TachesService;
import com.example.demo.services.UserService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjetsController {
    final private ProjetService projetService;
    final private UserService userService;
    final private TachesService tachesService;
    private final UserProjectRepository userProjectRepository;

    

    

    @PostMapping("/")
    public ResponseEntity<Project> createProject(@RequestBody Project project) {
        Project saved = projetService.save(project);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/edit")
    public ResponseEntity<Project> updateProject(@PathVariable UUID id, @RequestBody ProjectUpdateDTO dto) {
        Optional<Project> existingOpt = projetService.findById(id);
        if (existingOpt.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Project existing = existingOpt.get();

        existing.setTitle(dto.getTitle());
        existing.setDescription(dto.getDescription());
        existing.setDeadline(dto.getDeadline());

        Project updated = projetService.save(existing);

        return new ResponseEntity<>(updated, HttpStatus.OK);
    }





   @GetMapping("/{id}")
    public ResponseEntity<ProjectResponseDTO> getProject(@PathVariable UUID id) {
        Optional<Project> projectOpt = projetService.findById(id);
        if (projectOpt.isPresent()) {
            Project project = projectOpt.get();
            List<User> members = projetService.findMembers(id);
            return new ResponseEntity<>(new ProjectResponseDTO(project, members), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/owned")
    public List<Project> getOwnedProjects(@RequestParam UUID user_id){
        return projetService.findOwned(user_id);
    }

    @GetMapping("/member")
    public List<Project> getMemberProjects(@RequestParam UUID user_id){
        return projetService.findUserProjects(user_id);
    }

    @PostMapping("/addMember")
    public ResponseEntity<String> addMembers(@RequestBody UserProjectRelDTO data) {
    try {
        Optional<User> userOpt = userService.findById(data.userId);
        Optional<Project> projectOpt = projetService.findById(data.projectId);

        if (userOpt.isEmpty() || projectOpt.isEmpty()) {
            return new ResponseEntity<>("User or Project not found", HttpStatus.NOT_FOUND);
        }

        User user = userOpt.get();
        Project project = projectOpt.get();

        // ✅ Check if the relation already exists
        if (userProjectRepository.existsByUserAndProject(user, project)) {
            return new ResponseEntity<>("User is already a member of the project", HttpStatus.CONFLICT);
        }

        // ✅ Otherwise, create the new link
        UserProject link = new UserProject(user, project, "REGULAR");
        userProjectRepository.save(link);

        return new ResponseEntity<>("Member added successfully", HttpStatus.OK);

    } catch (Exception e) {
        return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}


    @DeleteMapping("/removeMember")
    @Transactional                      
    public ResponseEntity<String> removeMember(@RequestBody UserProjectRelDTO data) {

        Optional<User>    userOpt    = userService.findById(data.userId);
        Optional<Project> projectOpt = projetService.findById(data.projectId);

        if (userOpt.isPresent() && projectOpt.isPresent()) {
            userProjectRepository.deleteByUserAndProject(userOpt.get(), projectOpt.get());
            return new ResponseEntity<>("Member removed", HttpStatus.OK);
        }
        return new ResponseEntity<>("User or Project not found", HttpStatus.NOT_FOUND);
    }

    @PostMapping("/{project_id}/addTask")
    public ResponseEntity<Task> addTask(@PathVariable UUID project_id, @RequestBody Task task){
        try {
           Task created = tachesService.ajouterService(task);
            return new ResponseEntity<>(created,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }



    @DeleteMapping("/deleteAllOwned")
    public ResponseEntity<String> deleteAllOwned(@RequestParam UUID user_id){
        projetService.deleteAllOwnedByUser(user_id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @DeleteMapping("/deleteById")
    public ResponseEntity<String> deleteById(@RequestBody UUID project_id){
        try {
            projetService.deleteById(project_id);
            tachesService.suprimerTous(project_id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Project not found", HttpStatus.NOT_FOUND);
        }
    }

   
}
