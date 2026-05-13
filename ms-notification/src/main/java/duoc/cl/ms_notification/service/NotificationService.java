package duoc.cl.ms_notification.service;

import duoc.cl.ms_notification.dto.NotificationRequestDto;
import duoc.cl.ms_notification.dto.NotificationResponseDto;

import java.util.List;

public interface NotificationService {
    List<NotificationResponseDto> findAll();
    NotificationResponseDto findById(Long id);
    void send(NotificationRequestDto Notification);
}
