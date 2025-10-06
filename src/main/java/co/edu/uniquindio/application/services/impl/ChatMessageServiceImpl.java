package co.edu.uniquindio.application.services.impl;

import co.edu.uniquindio.application.dto.ChatMessageDTO;
import co.edu.uniquindio.application.dto.externalServiceDTO.SendEmailDTO;
import co.edu.uniquindio.application.exceptions.ForbiddenException;
import co.edu.uniquindio.application.exceptions.ResourceNotFoundException;
import co.edu.uniquindio.application.model.Booking;
import co.edu.uniquindio.application.model.ChatMessage;
import co.edu.uniquindio.application.model.User;
import co.edu.uniquindio.application.model.enums.BookingState;
import co.edu.uniquindio.application.model.enums.MessageStatus;
import co.edu.uniquindio.application.repositories.BookingRepository;
import co.edu.uniquindio.application.repositories.ChatMessageRepository;
import co.edu.uniquindio.application.repositories.UserRepository;
import co.edu.uniquindio.application.services.ActiveUserService;
import co.edu.uniquindio.application.services.ChatMessageService;
import co.edu.uniquindio.application.services.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {

    private final SimpMessagingTemplate simpleMessagingTemplate;
    private final ChatMessageRepository chatMessageRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ActiveUserService  activeUserService;
    private final EmailService emailService;


    @Override
    public void sendMessage(String bookingId, ChatMessageDTO message) throws Exception {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Reserva no encontrada"));

        if (booking.getBookingState() != BookingState.PENDING) {
            throw new ForbiddenException("Chat solo disponible para reservas confirmadas (pendientes)");
        }

        User sender = booking.getUser();
        User receiver = userRepository.findById(message.receiverId()).orElseThrow(() -> new ResourceNotFoundException("Destinatario no encontrado"));
        ChatMessage saved = chatMessageRepository.save(
                new ChatMessage(UUID.randomUUID().toString(), booking, sender,
                        receiver, message.content(), LocalDateTime.now(), MessageStatus.SENT)
        );

        if(activeUserService.isActive(message.receiverId())){
            simpleMessagingTemplate.convertAndSendToUser(
                    message.receiverId(), "/queue/messages", saved
            );
        }else {


            SendEmailDTO sendEmailDTO = new SendEmailDTO("Nuevo mensaje", "hola, "+receiver.getName()+" acabas de recibir un mensaje de parte de: "+sender.getName()+ ":\n\n" +
                                                        " sobre la reserva del aojamiento: "+booking.getAccommodation().getTitle(), receiver.getEmail() );
            emailService.sendMail(sendEmailDTO);
        }


    }
}
