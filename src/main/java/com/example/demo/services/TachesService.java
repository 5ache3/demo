package com.example.demo.services;

import com.example.demo.models.Project;
import com.example.demo.models.Task;
import com.example.demo.models.User;
import com.example.demo.models.UserTask;

import com.example.demo.repositories.TachesRepository;
import com.example.demo.repositories.UserTaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Service
public class TachesService{

     private TachesRepository tachesRepository;
     private UserTaskRepository userTaskRepository;
     public TachesService(TachesRepository tachesRepository, UserTaskRepository userTaskRepository){
         this.tachesRepository=tachesRepository;
         this.userTaskRepository=userTaskRepository;
     }
     public void ajouterService(Task taches){
         tachesRepository.save(taches);
     }
    //  public boolean  modifierService(Task taches){
    //      Optional<Task> optional=tachesRepository.findByTask(taches.getId());
    //      if(optional.isPresent()){
    //          tachesRepository.save(taches);
    //          return true;
    //      }else {
    //          return false;
    //      }
    //  }
     
     public boolean ajouterMembresService(List<User> membres, UUID tache_id){
         Optional<Task> optional=tachesRepository.findById(tache_id);
         if (optional.isPresent()) {
             Task tache = optional.get();
             for (User membre : membres) {
                 UserTask userTask = new UserTask();
                 userTask.setUser(membre);
                 userTask.setTask(tache);
                 userTaskRepository.save(userTask);
             }
             return true;
         }else{
             return  false;
         }
     }

    public  boolean modifierTitreService(String titre, UUID tache_id){
        Optional<Task> optional=tachesRepository.findById(tache_id);
        if (optional.isPresent()) {
            optional.get().setTitle(titre);
            tachesRepository.save(optional.get());
            return true;
        }else{
            return  false;
        }
    }
    public  boolean  modifierDescriptionService(String description, UUID tache_id){
        Optional<Task> optional=tachesRepository.findById(tache_id);
        if (optional.isPresent()) {
            optional.get().setDescription(description);
            tachesRepository.save(optional.get());
            return true;
        }else{
            return  false;
        }
    }
    // public  boolean modifierDateEcheanceService(LocalDate date, UUID tache_id){
    //     Optional<Taches> optional=tachesRepository.findById(tache_id);
    //     if (optional.isPresent()) {
    //         optional.get().setDateEcheance(date);
    //         tachesRepository.save(optional.get());
    //         return true;
    //     }else{
    //         return  false;
    //     }
    // }
    // public boolean modifierStatutService(UUID tache_id){
    //     Optional<Task> optional =tachesRepository.findById(tache_id);
    //     if(optional.isPresent()){
    //         optional.get().setStatut(Statut.TERMINE);
    //         return  true;
    //     }else {
    //         return false;

    //     }
    // }

    // public List<Task> afficherService(UUID user_id){
    //      User users=new User();
    //      users.setId(user_id);
    //      return  tachesRepository.findByCreateParOrAssigneA(users,users);
    //  }
     public  void suprimerService(UUID tache_id){
         tachesRepository.deleteById(tache_id);
     }
     public  void suprimerTous(UUID projet_id){
         Project projets=new Project();
         projets.setId(projet_id);
         tachesRepository.deleteByProject(projets);
     }





}
