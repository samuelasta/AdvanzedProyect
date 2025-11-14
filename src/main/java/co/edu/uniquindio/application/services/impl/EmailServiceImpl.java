package co.edu.uniquindio.application.services.impl;

import co.edu.uniquindio.application.dto.externalServiceDTO.SendEmailDTO;
import co.edu.uniquindio.application.services.EmailService;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.api.mailer.config.TransportStrategy;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Override
    @Async
    public void sendMail(SendEmailDTO sendEmailDTO) {
        try {
            Email email = EmailBuilder.startingBlank()
                    .from("no_reply@bookings.com")
                    .to(sendEmailDTO.recipient())
                    .withSubject(sendEmailDTO.subject())
                    .withPlainText(sendEmailDTO.body())
                    .buildEmail();

            // SIN try-with-resources
            Mailer mailer = MailerBuilder
                    .withSMTPServer("smtp.gmail.com", 587,
                            "nataliaavanzadaprogramacion@gmail.com",
                            "npkhzcipisnyfbas")
                    .withTransportStrategy(TransportStrategy.SMTP_TLS)
                    .withDebugLogging(true)
                    .buildMailer();

            mailer.sendMail(email);

            System.out.println("Email enviado exitosamente a: " + sendEmailDTO.recipient());

        } catch (Exception e) {
            System.err.println("Error al enviar email:");
            e.printStackTrace();
        }
    }
}