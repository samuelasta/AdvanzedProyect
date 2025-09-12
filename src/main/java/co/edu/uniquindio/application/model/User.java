package co.edu.uniquindio.application.model;

import co.edu.uniquindio.application.model.enums.State;
import lombok.*;
import co.edu.uniquindio.application.model.enums.Role;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class User {

    private String id;
    private String name;
    private String email;
    private String phone;
    private LocalDate birthday;
    private String country;
    private Role role;
    private String password;
    private String profileUrl;
    private State state;
    private String createdAt;

}
