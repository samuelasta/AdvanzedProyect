package co.edu.uniquindio.application.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;

@Entity
public class PasswordResetCode {

    @Id
    private String id;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(optional = false)
    private User user;
}
