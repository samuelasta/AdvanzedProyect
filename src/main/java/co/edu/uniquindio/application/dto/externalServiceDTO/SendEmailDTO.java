package co.edu.uniquindio.application.dto.externalServiceDTO;

public record SendEmailDTO (
    String subject,
    String body,
    String recipient
    ){}
