package co.edu.uniquindio.application.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Location {

    private String country;
    private String department;
    private String city;
    private String neighborhood;
    private String street;
    private String postalCode;

    @Embedded
    private Coordinates coordinates;

}
