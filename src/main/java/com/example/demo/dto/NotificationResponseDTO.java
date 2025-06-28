package com.example.demo.dto;

import java.util.UUID;

public class NotificationResponseDTO {
    public UUID project_id;
    public UUID destinateur_id;
    public UUID sender_id;
    public String title;
    public String message;
    public String type;
    public String state;
}
