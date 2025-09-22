package co.edu.uniquindio.application.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@AllArgsConstructor
@Builder
@NoArgsConstructor
@Getter
@Entity
public class Comment {

    @Id
    private String id;

    @Lob//especifica que puede ser un texto largo
    @Column(nullable = false)
    private String comment;

    @Column(nullable = false)
    private LocalDateTime commentDate;

    @Column(nullable = false)
    private int rating;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Accommodation accommodation;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;
}
