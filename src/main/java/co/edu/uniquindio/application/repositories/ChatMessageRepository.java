package co.edu.uniquindio.application.repositories;

import co.edu.uniquindio.application.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, String> {
}
