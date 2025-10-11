package co.edu.uniquindio.application.test;

import co.edu.uniquindio.application.dto.externalServiceDTO.SendEmailDTO;
import co.edu.uniquindio.application.services.impl.EmailServiceImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class EmailServiceImplTest {

    @Test
    void testSendMail_executesWithoutException() {
        System.setProperty("simplejavamail.transport.mode.logging.only", "true");

        // El orden correcto es subject, body, recipient
        SendEmailDTO dto = new SendEmailDTO(
                "Asunto de prueba",
                "Cuerpo del correo",
                "test@correo.com"
        );

        EmailServiceImpl service = new EmailServiceImpl();

        assertDoesNotThrow(() -> service.sendMail(dto));
    }

}
