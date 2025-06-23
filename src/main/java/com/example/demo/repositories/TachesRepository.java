package com.example.demo.repositories;

import com.example.demo.models.Project;
import com.example.demo.models.Task;
import com.example.demo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TachesRepository extends JpaRepository<Task, UUID> {
    void  deleteByProject(Project projets);
    // Task findByTask(Task task);
}
