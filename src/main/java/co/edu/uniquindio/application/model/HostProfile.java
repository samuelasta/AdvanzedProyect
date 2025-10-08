package co.edu.uniquindio.application.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class
HostProfile {

    @Id
    private String id;

    @OneToOne
    //@MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @ElementCollection
    @CollectionTable(joinColumns = @JoinColumn(name = "hostProfileId"))
    @Column(name = "document")
    private List<String> documents;

}
