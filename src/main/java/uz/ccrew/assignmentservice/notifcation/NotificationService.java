package uz.ccrew.assignmentservice.notifcation;

import java.util.List;

/**
 * Service is async
 */
public interface NotificationService {
    void sendNotification(List<String> users, String message);

    void sendNotification(String user, String json);
}
