package co.edu.uniquindio.application.services;

import co.edu.uniquindio.application.dto.ChatMessageDTO;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface ChatMessageService {

    void sendMessage(@DestinationVariable String bookingId, ChatMessageDTO message) throws Exception;
}
