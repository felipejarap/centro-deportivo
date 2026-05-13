package duoc.cl.ms_notification.service.impl;

import duoc.cl.ms_notification.dto.NotificationRequestDto;
import duoc.cl.ms_notification.dto.NotificationResponseDto;
import duoc.cl.ms_notification.model.Notification;
import duoc.cl.ms_notification.repository.NotificationRepository;
import duoc.cl.ms_notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository repository;
    private final JavaMailSender mail;


    private Notification toEntity(NotificationResponseDto dto) {
        return new Notification(
                dto.getId(),
                dto.getTo(),
                dto.getSubject(),
                dto.getBody(),
                dto.getSendDate()




        );
    }

    private Notification toEntity(NotificationRequestDto dto) {
        Notification entity = new Notification();
        entity.setTo(dto.getTo());
        entity.setSubject(dto.getSubject());
        entity.setBody(dto.getBody());
        return entity;
    }

    private NotificationResponseDto toDto(Notification entity) {
        return new NotificationResponseDto(
                entity.getId(),
                entity.getTo(),
                entity.getSubject(),
                entity.getBody(),
                entity.getSendDate()
        );
    }

    @Override
    public List<NotificationResponseDto> findAll() {
        return repository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public NotificationResponseDto findById(Long id) {
        return repository.findById(id).map(this::toDto).orElse(null);
    }

    @Override
    public void send(NotificationRequestDto dto) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(dto.getTo());
        message.setSubject(dto.getSubject());
        message.setText(dto.getBody());
        mail.send(message);


        Notification entity = toEntity(dto);
        entity.setSendDate(LocalDateTime.now());

        repository.save(entity);

    }
}
