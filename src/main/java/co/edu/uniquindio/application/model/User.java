package co.edu.uniquindio.application.model;

import co.edu.uniquindio.application.model.enums.State;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import co.edu.uniquindio.application.model.enums.Role;

import java.time.LocalDate;

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
