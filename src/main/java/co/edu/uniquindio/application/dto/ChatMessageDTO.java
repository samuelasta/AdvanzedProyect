package co.edu.uniquindio.application.dto;

public record ChatMessageDTO(String senderId,
                             String receiverId,
                             String content) {
}
