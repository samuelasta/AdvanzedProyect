package co.edu.uniquindio.application.services;

import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

// servicio que guarda los usuarios activos
@Service
public class ActiveUserService {
    private final Set<String> activeUsers = ConcurrentHashMap.newKeySet();

    public void add(String userId) { activeUsers.add(userId); }
    public void remove(String userId) { activeUsers.remove(userId); }
    public boolean isActive(String userId) { return activeUsers.contains(userId); }

}
