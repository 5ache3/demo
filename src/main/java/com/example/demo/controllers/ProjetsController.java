package com.example.demo.controllers;


import com.example.demo.dto.ProjectResponseDTO;
import com.example.demo.models.Project;
import com.example.demo.models.User;
import com.example.demo.models.UserProject;
import com.example.demo.repositories.UserProjectRepository;
import com.example.demo.services.ProjetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/projects")
public class ProjetsController {
    final private ProjetService projetService;
    public ProjetsController(ProjetService  projetService){
        this.projetService=projetService;
    }

    

    @PostMapping("/")
    public ResponseEntity<Project> createProject(@RequestBody Project project) {
        Project saved = projetService.save(project);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
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

    @PostMapping("/addMembers")
    public ResponseEntity<String> addMembers(@RequestParam UUID project_id, @RequestBody List<User> members){
        try {
            projetService.addMembers(project_id, members);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Project not found", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/deleteAllOwned")
    public ResponseEntity<String> deleteAllOwned(@RequestParam UUID user_id){
        projetService.deleteAllOwnedByUser(user_id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/deleteById")
    public ResponseEntity<String> deleteById(@RequestParam UUID project_id){
        try {
            projetService.deleteById(project_id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Project not found", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/rename")
    public ResponseEntity<String> rename(@RequestParam UUID project_id, @RequestParam String name){
        try {
            projetService.rename(project_id, name);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Project not found", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/updateDescription")
    public ResponseEntity<String> updateDescription(@RequestParam UUID project_id, @RequestParam String description){
        try {
            projetService.updateDescription(project_id, description);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Project not found", HttpStatus.NOT_FOUND);
        }
    }

    // @GetMapping("/afficherProprietaire")
    // public  List<Project> afficherProprietaireProjets( @RequestParam  UUID user_id){
    //     return projetService.afficherProprietaireProjetsService(user_id);

    // }

    @GetMapping("/afficherMembresProjets")
    public  List<User> afficherMembresProjets(@RequestParam UUID project_id){
        return projetService.findMembers(project_id);
    }

    
    // @PostMapping("/ajouterMembre")
    // public  void ajouterMembre(@RequestBody  List<User> membres ,@RequestParam  UUID projet_id){
    //     projetService.addMembers(projet_id,membres);
    // }

    // @GetMapping("/modifierStatut")
    // public ResponseEntity<String> modifierStatut(@RequestParam UUID projet_id){
    //     if(projetService.modifierStatutService(projet_id)){
    //         return new ResponseEntity<>(HttpStatus.OK);
    //     }else {
    //         return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
    //     }
    // }
    // @PutMapping("/modifiermessage")
    // public  ResponseEntity<Project>  modifierMessage(@RequestParam  UUID user_id ,@RequestParam String message){
    //     if(projetService.modifierNomService(user_id,message)){
    //         return new ResponseEntity<>(HttpStatus.OK);
    //     }else {
    //         return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
    //     }
    // }
    // @PutMapping("/modifier")
    // public  ResponseEntity<String> modifierProjet(@RequestBody  Project projets){
    //     if(projetService.modifierProjetService(projets)){
    //         return  new ResponseEntity<>(HttpStatus.OK);
    //     }else {
    //         return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    //     }
    // }
}
