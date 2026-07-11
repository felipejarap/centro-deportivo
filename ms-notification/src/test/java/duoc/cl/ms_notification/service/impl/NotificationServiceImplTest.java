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

// Habilita el entorno de pruebas unitarias puras con Mockito en JUnit 5
@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTest {

    // Simula el repositorio local de la tabla de notificaciones históricas
    @Mock
    private NotificationRepository repository;

    // Doble de prueba para evitar el envío real de correos electrónicos por protocolo SMTP
    @Mock
    private JavaMailSender mail;

    // Inyecta los mocks de base de datos y mensajería automáticamente dentro del servicio
    @InjectMocks
    private NotificationServiceImpl service;

    private Notification notificationEntity;
    private NotificationRequestDto requestDto;

    // Configuración inicial de las entidades requeridas antes de cada ejecución
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

    // =========================================================================
    // PRUEBAS: METODO findAll()
    // =========================================================================

    /**
     * BUSCAR TODOS: Verifica que el servicio acceda al repositorio, recupere el listado
     * histórico de correos enviados y los exponga mapeados en DTOs.
     */
    @Test
    void givenExistingNotifications_whenFindAll_thenReturnList() {
        // GIVEN: El repositorio local contiene un registro de notificación guardado
        when(repository.findAll()).thenReturn(List.of(notificationEntity));

        // WHEN: Invocamos el método del servicio para listar el historial
        List<NotificationResponseDto> result = service.findAll();

        // THEN: Confirmamos el tamaño de la lista, el destinatario y el llamado único al repositorio
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("alumno@duocuc.cl", result.getFirst().getTo());
        verify(repository, times(1)).findAll();
    }

    // =========================================================================
    // PRUEBAS: METODO findById()
    // =========================================================================

    /**
     * BUSCAR POR ID EXISTENTE: Evalúa que al consultar una notificación por su clave primaria,
     * el Optional sea procesado y devuelva el asunto del mensaje de forma correcta.
     */
    @Test
    void givenExistingId_whenFindById_thenReturnDto() {
        // GIVEN: El ID provisto corresponde a una notificación existente en el sistema
        when(repository.findById(1L)).thenReturn(Optional.of(notificationEntity));

        // WHEN: Solicitamos la notificación por su ID único
        NotificationResponseDto result = service.findById(1L);

        // THEN: Validamos las propiedades básicas del DTO devuelto
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Confirmación de Taller", result.getSubject());
    }

    /**
     * BUSCAR ID VACÍO: Comprueba que al buscar un identificador inexistente, el servicio
     * intercepte el Optional vacío regresando un valor nulo controlado.
     */
    @Test
    void givenNonExistingId_whenFindById_thenReturnNull() {
        // GIVEN: El ID consultado no figura en los registros de la base de datos
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // WHEN: Ejecutamos la búsqueda en el servicio de notificaciones
        NotificationResponseDto result = service.findById(99L);

        // THEN: Verificamos que el retorno sea nulo
        assertNull(result);
    }

    // =========================================================================
    // PRUEBAS: METODO send() (Flujo de Correo Electrónico)
    // =========================================================================

    /**
     * ENVIAR CORREO EXITOSO: Caso de negocio crítico. Comprueba que al enviar una petición,
     * se gatille la interfaz de JavaMailSender y se guarde un registro histórico en la BD.
     */
    @Test
    void givenValidRequest_whenSend_thenMailIsSentAndNotificationIsSaved() {
        // GIVEN: Como mail.send es un método void, simulamos que opera con normalidad sin hacer nada
        doNothing().when(mail).send(any(SimpleMailMessage.class));
        when(repository.save(any(Notification.class))).thenReturn(notificationEntity);

        // WHEN: Invocamos el envío controlando que el flujo no arroje ninguna excepción
        assertDoesNotThrow(() -> service.send(requestDto));

        // THEN: Aseguramos físicamente que el correo salió por red y la bitácora fue persistida una vez
        verify(mail, times(1)).send(any(SimpleMailMessage.class));
        verify(repository, times(1)).save(any(Notification.class));
    }
}
