package co.edu.uniquindio.application.model;

import co.edu.uniquindio.application.model.enums.State;
import co.edu.uniquindio.application.model.enums.AccommodationType;
import co.edu.uniquindio.application.model.enums.Amenities;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Accommodation {

    private String id;
    private Location location;
    private double price;
    private List<String> pics_url;
    private String description;
    private List<Amenities> amenities;
    private String title;
    private int capacity;
    private State state;
    private int totalRatings;
    private double averageRatings;
    private AccommodationType accommodationType;
    private User user;
    private String postal_code;
    private List<Booking> bookings;
    private LocalDateTime createdAt;
    private List<Comment> comments;
}
