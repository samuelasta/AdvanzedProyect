package co.edu.uniquindio.application.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Location {

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private String department;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String neighborhood;
    private String street;

    @Column(nullable = false)
    private String postalCode;

    @Embedded
    private Coordinates coordinates;

}
