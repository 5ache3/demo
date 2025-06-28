package com.example.demo.dto;


import java.time.LocalDateTime;

import lombok.Data;
@Data
public class ProjectUpdateDTO {
    private String title;
    private String description;
    private LocalDateTime deadline;
} 