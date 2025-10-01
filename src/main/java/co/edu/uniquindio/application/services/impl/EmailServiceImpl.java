package co.edu.uniquindio.application.services.impl;

import co.edu.uniquindio.application.dto.externalServiceDTO.SendEmailDTO;
import co.edu.uniquindio.application.services.EmailService;
//import jakarta.validation.constraints.Email;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.api.mailer.config.TransportStrategy;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import static org.simplejavamail.config.ConfigLoader.Property.SMTP_PORT;

@Service
public class EmailServiceImpl implements EmailService {
    @Override
    @Async
    public void sendMail(SendEmailDTO sendEmailDTO) throws Exception {
        Email email = EmailBuilder.startingBlank()
                .from("no_reply@bookings.com")
                .to(sendEmailDTO.recipient())
                .withSubject(sendEmailDTO.subject())
                .withPlainText(sendEmailDTO.body())
                .buildEmail();
        try(Mailer mailer = MailerBuilder
                .withSMTPServer("smtp.gmail.com", 587, "nataliaavanzadaprogramacion@gmail.com", "bzeakckafcqoyrgi")
                .withTransportStrategy(TransportStrategy.SMTP_TLS)
                .withDebugLogging(true)
                .buildMailer()) {
            mailer.sendMail(email);
        }
        }


    }

