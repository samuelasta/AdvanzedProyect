package co.edu.uniquindio.application.services.impl;

import co.edu.uniquindio.application.exceptions.ResourceNotFoundException;
import co.edu.uniquindio.application.services.CurrentUserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserServiceImpl implements CurrentUserService {

    @Override
    public String getCurrentUser() {
        //para sacar el id del token

            org.springframework.security.core.userdetails.User user =
                    (org.springframework.security.core.userdetails.User) SecurityContextHolder
                            .getContext().getAuthentication().getPrincipal();
            System.out.println(user.getUsername());
            System.out.println(user.getAuthorities());
            return user.getUsername(); // este es el id que se metió en el token
        }
    }

