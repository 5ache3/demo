package com.example.demo.repositories;

import com.example.demo.models.Project;
// import com.example.demo.models.Statut;
import com.example.demo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProjetsRepository extends JpaRepository<Project, UUID> {
  // List<Project> findByProprietaireAndStatut(User proprietaire, Statut statut);
  List<Project> findByOwner(User users);
  
}

