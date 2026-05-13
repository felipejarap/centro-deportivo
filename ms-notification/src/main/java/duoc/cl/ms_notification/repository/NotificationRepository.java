package duoc.cl.ms_notification.repository;

import duoc.cl.ms_notification.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
