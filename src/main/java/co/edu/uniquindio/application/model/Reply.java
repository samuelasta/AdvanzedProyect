package co.edu.uniquindio.application.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Reply {

    @Id
    private String id;

    @Column(nullable = false, length = 200)
    private String reply;

    @OneToOne
    @JoinColumn(name = "comment_id",unique = true, nullable = false)//unique me dice que solo una respuesta por comentario
    private Comment comment;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}
