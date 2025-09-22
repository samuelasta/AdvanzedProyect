package co.edu.uniquindio.application.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class HostProfile {

    @Id
    private String id;

    @OneToOne
    private User user;

    @Column(length = 200)
    private String description;

    @ElementCollection
    @CollectionTable(joinColumns = @JoinColumn(name = "hostProfileId"))
    @Column(name = "document")
    private List<String> documents;

}
