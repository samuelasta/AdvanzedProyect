package co.edu.uniquindio.application.model;

import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Coordinates {
    private float latitude;
    private float longitude;
}
