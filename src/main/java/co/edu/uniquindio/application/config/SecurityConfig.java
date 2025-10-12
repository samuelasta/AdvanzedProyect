package co.edu.uniquindio.application.config;

import co.edu.uniquindio.application.security.JwtAuthenticationEntryPoint;
import co.edu.uniquindio.application.security.JWTFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JWTFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Configura la seguridad HTTP para la aplicación
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(req -> req
                        .requestMatchers(HttpMethod.GET, "/api/accommodations/**").permitAll() //listar o .... gets
                        .requestMatchers("/api/auth/**").permitAll() //login y registro
                        .requestMatchers(HttpMethod.POST, "/api/accommodations/**").hasRole("HOST")
                                .requestMatchers("/api/bookings/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/users/*/bookings/**").hasAnyRole("USER", "HOST")
                                .requestMatchers(HttpMethod.GET, "/api/users/**").hasAnyRole("USER", "HOST")
                                .requestMatchers(HttpMethod.GET, "/api/users/me/bookings/**").hasAnyRole("USER", "HOST")
                                .requestMatchers(HttpMethod.GET, "/api/users/*/bookings/**").hasAnyRole("USER", "HOST")
                                .requestMatchers(HttpMethod.POST, "/api/users/me/bookings/**").hasAnyRole("USER", "HOST")

                                .requestMatchers(
                                        "/api/auth/**",       // login, registro
                                        "/ws-chat/**",        // endpoint WebSocket
                                        "/app/**"             // destino STOMP del cliente
                                ).permitAll()
                                .requestMatchers("api/favorites/**").hasAnyRole("USER", "HOST")
                        .anyRequest().authenticated()
                        //.requestMatchers("/api/admin/**").hasRole("ADMIN")
                      //  .requestMatchers("/api/bookings/**").hasAnyRole("USER", "HOST")
                )
                .exceptionHandling(ex -> ex.authenticationEntryPoint(new JwtAuthenticationEntryPoint()))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        // Configura las políticas de CORS para permitir solicitudes desde el frontend
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Permite codificar y verificar contraseñas utilizando BCrypt
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
            throws Exception {
        // Proporciona un AuthenticationManager para la autenticación de usuarios
        return configuration.getAuthenticationManager();
    }
}
