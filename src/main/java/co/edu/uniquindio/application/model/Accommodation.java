package co.edu.uniquindio.application.model;

import co.edu.uniquindio.application.model.enums.State;
import co.edu.uniquindio.application.model.enums.AccommodationType;
import co.edu.uniquindio.application.model.enums.Amenities;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Accommodation {

    @Id
    private String id;

    @Column(nullable = false)
    @Embedded
    private Location location;

    @Column(nullable = false)
    private double price;

    @ElementCollection
    @CollectionTable(
            name = "pics_urls", // tabla intermedia
            joinColumns = @JoinColumn(name = "accommodation_id") // FK a Accommodation
    )
    @Column(name = "pics_url")
    private List<String> pics_url;

    @Lob
    @Column(nullable = false)
    private String description;

    @ElementCollection
    @CollectionTable(joinColumns = @JoinColumn(name = "accommodation_id"))
    @Column(name = "amenitie")
    private List<Amenities> amenities;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false)
    private int capacity;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private State state;

    @Column(nullable = false)
    private int totalRatings;


    @Column(nullable = false)
    private double averageRatings;

    @Column(nullable = false)
    private AccommodationType accommodationType;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "accommodation")
    private List<Comment> comments;
}
