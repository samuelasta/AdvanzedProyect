package co.edu.uniquindio.application.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;


public class PasswordResetCode {

    private String id;

    private String code;

    private LocalDateTime createdAt;

    private User user;
}
