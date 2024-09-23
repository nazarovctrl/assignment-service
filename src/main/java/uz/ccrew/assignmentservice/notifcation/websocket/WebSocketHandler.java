package uz.ccrew.assignmentservice.notifcation.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.util.Map;
import java.io.IOException;
import java.security.Principal;

@RequiredArgsConstructor
public class WebSocketHandler extends AbstractWebSocketHandler {

    private final Map<String, WebSocketSession> sessions;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws IOException {
        Principal principal = session.getPrincipal();

        if (principal == null || principal.getName() == null) {
            session.close(CloseStatus.SERVER_ERROR.withReason("User must be authenticated"));
            return;
        }
        sessions.put(principal.getName(), session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        Principal principal = session.getPrincipal();
        sessions.remove(principal.getName());
    }
}