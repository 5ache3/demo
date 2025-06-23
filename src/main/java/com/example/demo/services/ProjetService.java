package com.example.demo.services;

import com.example.demo.models.*;
import com.example.demo.repositories.ProjetsRepository;
import com.example.demo.repositories.UserProjectRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor      // generates constructor for final fields
public class ProjetService {

    private final ProjetsRepository projetsRepository;
    private final UserProjectRepository userProjectRepository;

    public List<User> findMembers(UUID projectId) {
    Optional<Project> projectOptional = projetsRepository.findById(projectId);
    
    if (projectOptional.isEmpty()) {
        return List.of(); // or throw an exception
    }

    Project project = projectOptional.get();
    List<UserProject> userProjects = userProjectRepository.findByProject(project);

    return userProjects.stream()
        .map(UserProject::getUser)
        .toList();
}
    @Transactional
    public List<Project> findAllForUser(UUID userId) {
        User userRef = new User();
        userRef.setId(userId);

        List<Project> owned   = projetsRepository.findByOwner(userRef);
        List<Project> member  = userProjectRepository.findByUser(userRef)
                                                     .stream()
                                                     .map(UserProject::getProject)
                                                     .collect(Collectors.toList());

        // merge & remove duplicates
        Set<Project> all = new HashSet<>(owned);
        all.addAll(member);
        return new ArrayList<>(all);
    }

    /** Projects the user owns */
    @Transactional
    public List<Project> findOwned(UUID userId) {
        User owner = new User();
        owner.setId(userId);
        return projetsRepository.findByOwner(owner);
    }

    /** Projects where the user is *only* a member (not owner) */
    @Transactional
    public List<Project> findUserProjects(UUID userId) {
        User user = new User();
        user.setId(userId);
        return userProjectRepository.findByUser(user)
                                    .stream()
                                    .map(UserProject::getProject)
                                    .filter(p -> !p.getOwner().getId().equals(userId))
                                    .collect(Collectors.toList());
    }

    @Transactional
    public Project save(Project projet) {
        return projetsRepository.save(projet);
    }

    @Transactional
    public void addMembers(UUID projectId, List<User> members) {
        Project project = projetsRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Projet introuvable"));

        for (User member : members) {
            boolean alreadyMember = userProjectRepository.existsByUserAndProject(member,project);
            if (!alreadyMember) {
                UserProject up = new UserProject();
                up.setProject(project);
                up.setUser(member);
                up.setRole("MEMBRE");
                userProjectRepository.save(up);
            }
        }
    }

    @Transactional
    public void deleteAllOwnedByUser(UUID ownerId) {
        User owner = new User(); owner.setId(ownerId);
        projetsRepository.deleteAll(projetsRepository.findByOwner(owner));
    }

    @Transactional
    public boolean deleteById(UUID projectId) {
        if (!projetsRepository.existsById(projectId)) {
            throw new EntityNotFoundException("Projet introuvable");
        }
        projetsRepository.deleteById(projectId);
        return true;
    }

    @Transactional
    public void rename(UUID projectId, String nouveauNom) {
        Project p = fetch(projectId);
        p.setTitle(nouveauNom);
    }

    @Transactional
    public void updateDescription(UUID projectId, String nouvelleDescription) {
        Project p = fetch(projectId);
        p.setDescription(nouvelleDescription);
    }

    private Project fetch(UUID id) {
        return projetsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Projet introuvable"));
    }


}
