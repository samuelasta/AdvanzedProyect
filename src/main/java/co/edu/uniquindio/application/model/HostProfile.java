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
    private User user;

    @ElementCollection
    @CollectionTable(joinColumns = @JoinColumn(name = "hostProfileDocuments"))
    @Column(name = "document")
    private List<String> documents;

}
