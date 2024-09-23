package uz.ccrew.assignmentservice.notifcation;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.List;
import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final Map<String, WebSocketSession> sessions;

    @Async
    @Override
    public void sendNotification(List<String> users, String json) {
        users.forEach(user -> sendNotification(user, json));
    }

    @Override
    public void sendNotification(String user, String json) {
        WebSocketSession session = sessions.get(user);
        if (session == null) {
            log.warn("User {} did not receive a notification, Because user's session not found", user);
            return;
        }
        try {
            session.sendMessage(new TextMessage(json));
        } catch (IOException e) {
            log.warn("User {} did not receive a notification", user);
        }
    }
}