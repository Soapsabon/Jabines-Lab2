package edu.cit.jabines.notification.controller;

import edu.cit.jabines.notification.model.Notification;
import edu.cit.jabines.notification.repository.NotificationRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "http://localhost:5173")
public class NotificationController {

    private final NotificationRepository notificationRepository;

    public NotificationController(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @GetMapping
    public List<Notification> getNotifications() {
        return notificationRepository.findAll().stream()
                .sorted(Comparator.comparing(Notification::getCreatedAt).reversed())
                .toList();
    }
}