package com.example.demo.repositories;

import com.example.demo.models.UserTask;
import com.example.demo.models.User;
import com.example.demo.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserTaskRepository extends JpaRepository<UserTask, UUID> {
    List<UserTask> findByUser(User user);
    List<UserTask> findByTask(Task task);
    UserTask findByUserAndTask(User user, Task task);
} 