package com.example.demo.controllers;

import com.example.demo.models.Task;
import com.example.demo.models.User;
import com.example.demo.repositories.TachesRepository;
import com.example.demo.services.TachesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/taches")
public class TachesController {
    final private TachesService tachesService;
    public TachesController(TachesService tachesService){
        this.tachesService=tachesService;
    }
    @PostMapping("/ajouter")
    public ResponseEntity<String> ajouter(@RequestBody Task tache){
        tachesService.ajouterService(tache);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @PutMapping("/ajoutermembre")
    public ResponseEntity<String> ajouterMembre(@RequestBody List<User> membres, @RequestParam UUID tache_id){
        if(tachesService.ajouterMembresService(membres, tache_id)){
            return new ResponseEntity<>(HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping("/modifiertitre")
    public ResponseEntity<String> modifierTitre(@RequestParam String titre, @RequestParam UUID tache_id){
        if(tachesService.modifierTitreService(titre, tache_id)){
            return new ResponseEntity<>(HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping("/modifierdescription")
    public ResponseEntity<String> modifierDescription(@RequestParam String description, @RequestParam UUID tache_id){
        if(tachesService.modifierDescriptionService(description, tache_id)){
            return new ResponseEntity<>(HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping("/suprimer")
    public ResponseEntity<String> suprimer(@RequestParam UUID tache_id){
        tachesService.suprimerService(tache_id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @DeleteMapping("/suprimertous")
    public ResponseEntity<String> suprimerTous(@RequestParam UUID projet_id){
        tachesService.suprimerTous(projet_id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
