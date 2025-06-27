package com.example.demo.controllers;

import com.example.demo.models.Task;
import com.example.demo.models.User;
import com.example.demo.services.TachesService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/tasks")
public class TachesController {
    private static final Logger logger = Logger.getLogger(TachesController.class.getName());
    final private TachesService tachesService;
    public TachesController(TachesService tachesService){
        this.tachesService=tachesService;
    }
    @DeleteMapping("/delete")
    public ResponseEntity<String> suprimer(@RequestBody UUID task_id){
        tachesService.suprimerService(task_id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{task_id}/update")
    public ResponseEntity<String> update(@PathVariable UUID task_id,@RequestBody Task task){
        if(tachesService.update(task_id,task)){
            return new ResponseEntity<>("updated",HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
    }
    
}
