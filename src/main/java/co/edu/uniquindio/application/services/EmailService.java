package co.edu.uniquindio.application.services;

import co.edu.uniquindio.application.dto.externalServiceDTO.SendEmailDTO;
import co.edu.uniquindio.application.model.Booking;
import co.edu.uniquindio.application.model.User;

public interface EmailService {
    void sendMail(SendEmailDTO sendEmailDTO) throws Exception;

}
