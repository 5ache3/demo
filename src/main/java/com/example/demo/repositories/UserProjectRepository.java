package com.example.demo.repositories;

import com.example.demo.models.UserProject;
import com.example.demo.models.User;
import com.example.demo.models.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserProjectRepository extends JpaRepository<UserProject, UUID> {

    /* look‑ups */
    List<UserProject> findByUser(User user);
    List<UserProject> findByProject(Project project);
    UserProject findByUserAndProject(User user, Project project);
    boolean existsByUserAndProject(User user, Project project);
    
    void deleteByUserAndProject(User user, Project project);
}
