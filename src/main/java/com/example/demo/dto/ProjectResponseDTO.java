package com.example.demo.dto;

import com.example.demo.models.Project;
import com.example.demo.models.Task;
import com.example.demo.models.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class ProjectResponseDTO {

    /* ───────── Project fields ───────── */
    private UUID id;
    private String title;
    private String ownerId;
    private LocalDateTime createdAt;
    private String description;
    private LocalDateTime deadline;
    private int t;   // number of tasks
    private int c;   // number of comments (or whatever “c” means)

    /* ───────── Members list ───────── */
    private List<UserBasicDTO> members;

    /* Constructor that converts a Project + member list */
    public ProjectResponseDTO(Project project, List<User> memberEntities) {
    this.id          = project.getId();
    this.title       = project.getTitle();
    this.ownerId     = project.getOwnerId();           // or project.getOwner().getId()
    this.createdAt   = project.getCreatedAt();
    this.description = project.getDescription();
    this.deadline    = project.getDeadline();

    /* total tasks */
    this.t = project.getTasks() != null ? project.getTasks().size() : 0;

    /* ✔ tasks completed */
    if (project.getTasks() != null) {
        this.c = (int) project.getTasks()
                              .stream()
                              .filter(Task::isCompleted) // or task -> task.getStatus() == DONE
                              .count();
    } else {
        this.c = 0;
    }

    this.members = memberEntities.stream()
                             .map(UserBasicDTO::new)
                             .toList();

    for (int i = 0; i < members.size(); i++) {
        UserBasicDTO member = members.get(i);
        String role = this.ownerId.equals(member.getId().toString()) ? "owner" : "user";
        member.setRole(role);
    }
}


    /* getters (or use Lombok @Getter / @Data) */

    public UUID getId()             { return id; }
    public String getTitle()        { return title; }
    public String getOwnerId()      { return ownerId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getDescription()  { return description; }
    public LocalDateTime getDeadline()  { return deadline; }
    public int getT()               { return t; }
    public int getC()               { return c; }
    public List<UserBasicDTO> getMembers() { return members; }
}
