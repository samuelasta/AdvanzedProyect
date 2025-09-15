package co.edu.uniquindio.application.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Location {

    private String country;
    private String department;
    private String city;
    private String neighborhood;
    private String street;
    private String postalCode;
    private Coordinates coordinates;

}
