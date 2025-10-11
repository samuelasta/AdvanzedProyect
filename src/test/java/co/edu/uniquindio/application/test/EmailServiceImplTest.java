package co.edu.uniquindio.application.test;

import co.edu.uniquindio.application.dto.externalServiceDTO.SendEmailDTO;
import co.edu.uniquindio.application.services.impl.EmailServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.api.mailer.MailerRegularBuilder;
import org.simplejavamail.mailer.MailerBuilder;

import static org.mockito.Mockito.*;

class EmailServiceImplTest {

    @Test
    void testSendMail_callsMailerSendMail_once() throws Exception {
        // Arrange: DTO de entrada
        SendEmailDTO dto = new SendEmailDTO(
                "test@correo.com",
                "Asunto de prueba",
                "Este es el cuerpo del correo"
        );

        // Mock del Mailer final (el que envía)
        Mailer mockMailer = mock(Mailer.class);

        // Mock estático del MailerBuilder y su builder encadenable
        try (MockedStatic<MailerBuilder> mockedStatic = mockStatic(MailerBuilder.class)) {
            // Usamos RETURNS_SELF para que todos los métodos encadenados devuelvan el mismo builder
            MailerRegularBuilder mockBuilder = mock(MailerRegularBuilder.class, RETURNS_SELF);

            // Cuando el código llame a withSMTPServer(...), devolvemos nuestro builder mock
            mockedStatic.when(() ->
                    MailerBuilder.withSMTPServer(anyString(), anyInt(), anyString(), anyString())
            ).thenReturn(mockBuilder);

            // Al final de la cadena, buildMailer() debe devolver nuestro Mailer mock
            when(mockBuilder.buildMailer()).thenReturn(mockMailer);

            // Act: ejecutamos el servicio real (sin modificar tu clase)
            EmailServiceImpl service = new EmailServiceImpl();
            service.sendMail(dto);

            // Assert: verificamos que se intentó enviar exactamente 1 vez
            verify(mockMailer, times(1)).sendMail(any());
            verifyNoMoreInteractions(mockMailer);
        }
    }
}
