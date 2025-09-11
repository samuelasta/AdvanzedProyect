package co.edu.uniquindio.application.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Comment {

    private String id;
    //private String booking_id;
    private String comment;
    private LocalDateTime commentDate;
    private int rating;
    private Accommodation accommodation;
    private User user;
}
