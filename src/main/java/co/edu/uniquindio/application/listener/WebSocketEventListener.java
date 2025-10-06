package co.edu.uniquindio.application.listener;

import co.edu.uniquindio.application.services.ActiveUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

// Es el servicio para rastrear los usuarios conectados
@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {

    private final ActiveUserService activeUserService;

    @EventListener
    public void handleConnect(SessionConnectedEvent event) {
        String username = event.getUser().getName();
        activeUserService.add(username);
        log.info("Usuario conectado: {}", username);
    }

    @EventListener
    public void handleDisconnect(SessionDisconnectEvent event) {
        String username = event.getUser().getName();
        activeUserService.remove(username);
        log.info(" Usuario desconectado: {}", username);
    }

}
