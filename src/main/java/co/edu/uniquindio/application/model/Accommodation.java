package co.edu.uniquindio.application.model;

import co.edu.uniquindio.application.model.enums.State;
import co.edu.uniquindio.application.model.enums.AccommodationType;
import co.edu.uniquindio.application.model.enums.Amenities;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
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

}
