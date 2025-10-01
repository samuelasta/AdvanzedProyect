package co.edu.uniquindio.application.services;

import co.edu.uniquindio.application.dto.externalServiceDTO.SendEmailDTO;

public interface EmailService {
    void sendMail(SendEmailDTO sendEmailDTO) throws Exception;
}
