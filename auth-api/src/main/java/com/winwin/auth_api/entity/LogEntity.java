package com.winwin.auth_api.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "user_requests_log")
@Data
public class LogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String inputText;
    private String outputText;
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "log_user_id")
    private User user;
}