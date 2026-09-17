package edu.cit.jabines.notification.repository;

import edu.cit.jabines.notification.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}