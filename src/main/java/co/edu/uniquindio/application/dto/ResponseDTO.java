package co.edu.uniquindio.application.dto;

public record ResponseDTO<T>(
        Boolean error, T message) {
}
