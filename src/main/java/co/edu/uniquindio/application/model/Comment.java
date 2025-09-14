package co.edu.uniquindio.application.model;

import lombok.*;

import java.time.LocalDateTime;


@AllArgsConstructor
@Builder
@NoArgsConstructor
@Getter
public class Comment {

    private String id;
    private String comment;
    private LocalDateTime commentDate;
    private int rating;
    private Accommodation accommodation;
    private User user;
}
