package duoc.cl.ms_notification.service.impl;

import duoc.cl.ms_notification.dto.NotificationRequestDto;
import duoc.cl.ms_notification.dto.NotificationResponseDto;
import duoc.cl.ms_notification.model.Notification;
import duoc.cl.ms_notification.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTest {

    @Mock
    private NotificationRepository repository;

    @Mock
    private JavaMailSender mail; // Doble de prueba para evitar el envío real de correos

    @InjectMocks
    private NotificationServiceImpl service;

    private Notification notificationEntity;
    private NotificationRequestDto requestDto;

    @BeforeEach
    void setUp() {
        notificationEntity = new Notification(
                1L,
                "alumno@duocuc.cl",
                "Confirmación de Taller",
                "Tu cupo ha sido reservado con éxito.",
                LocalDateTime.now()
        );

        requestDto = new NotificationRequestDto();
        requestDto.setTo("alumno@duocuc.cl");
        requestDto.setSubject("Confirmación de Taller");
        requestDto.setBody("Tu cupo ha sido reservado con éxito.");
    }

    // ==========================================
    // PRUEBAS POUR: findAll()
    // ==========================================
    @Test
    void givenExistingNotifications_whenFindAll_thenReturnList() {
        // GIVEN
        when(repository.findAll()).thenReturn(List.of(notificationEntity));

        // WHEN
        List<NotificationResponseDto> result = service.findAll();

        // THEN
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("alumno@duocuc.cl", result.get(0).getTo());
        verify(repository, times(1)).findAll();
    }

    // ==========================================
    // PRUEBAS POUR: findById()
    // ==========================================
    @Test
    void givenExistingId_whenFindById_thenReturnDto() {
        // GIVEN
        when(repository.findById(1L)).thenReturn(Optional.of(notificationEntity));

        // WHEN
        NotificationResponseDto result = service.findById(1L);

        // THEN
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Confirmación de Taller", result.getSubject());
    }

    @Test
    void givenNonExistingId_whenFindById_thenReturnNull() {
        // GIVEN
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // WHEN
        NotificationResponseDto result = service.findById(99L);

        // THEN
        assertNull(result);
    }

    // ==========================================
    // PRUEBAS POUR: send() (Flujo de Correo Electrónico)
    // ==========================================
    @Test
    void givenValidRequest_whenSend_thenMailIsSentAndNotificationIsSaved() {
        // GIVEN
        // Como el método 'mail.send' es de tipo void, configuramos Mockito para simular que no hace nada
        doNothing().when(mail).send(any(SimpleMailMessage.class));
        when(repository.save(any(Notification.class))).thenReturn(notificationEntity);

        // WHEN
        // El método service.send(dto) retorna void, así que solo lo invocamos directamente
        assertDoesNotThrow(() -> service.send(requestDto));

        // THEN
        // Verificamos que se haya gatillado efectivamente el envío de correo y el guardado en la BD
        verify(mail, times(1)).send(any(SimpleMailMessage.class));
        verify(repository, times(1)).save(any(Notification.class));
    }
}
