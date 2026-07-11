package cl.duoc.ms.asistencia.service.impl;

import cl.duoc.ms.asistencia.dto.AssistanceRequestDto;
import cl.duoc.ms.asistencia.dto.AssistanceResponseDto;
import cl.duoc.ms.asistencia.dto.ClasseResponseDto;
import cl.duoc.ms.asistencia.dto.UserResponseDto;
import cl.duoc.ms.asistencia.model.Assistance;
import cl.duoc.ms.asistencia.repository.AssistanceRepository;
import cl.duoc.ms.asistencia.service.api.ClasseClient;
import cl.duoc.ms.asistencia.service.api.UserClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssistanceServiceImplTest {

    @Mock
    private AssistanceRepository repository;

    @Mock
    private UserClient userClient;

    @Mock
    private ClasseClient classeClient;

    @InjectMocks
    private AssistanceServiceImpl service;

    private Assistance assistanceEntity;
    private AssistanceRequestDto requestDto;
    private UserResponseDto mockUser;
    private ClasseResponseDto mockClasse;
    private LocalDateTime arrivalTime;

    @BeforeEach
    void setUp() {
        arrivalTime = LocalDateTime.of(2026, 6, 21, 10, 0);

        assistanceEntity = new Assistance();
        assistanceEntity.setIdAssistance(1L);
        assistanceEntity.setIdUser(10L);
        assistanceEntity.setIdClasse(5L);
        assistanceEntity.setArrivalTime(arrivalTime);
        assistanceEntity.setAssist(true);

        requestDto = new AssistanceRequestDto();
        requestDto.setIdUser(10L);
        requestDto.setIdClasse(5L);
        requestDto.setArrivalTime(arrivalTime);
        requestDto.setAssist(true);

        mockUser = new UserResponseDto();
        mockUser.setId(10L);
        mockUser.setUsername("felipe.perez");

        mockClasse = new ClasseResponseDto();
        mockClasse.setId(5L);
    }

    // ==========================================
    // PRUEBAS: findAll() y findById() (Enriquecimiento Exitoso)
    // ==========================================
    @Test
    void givenExistingAssistance_whenFindAll_thenReturnEnrichedList() {
        // GIVEN
        when(repository.findAll()).thenReturn(List.of(assistanceEntity));
        when(userClient.findById(10L)).thenReturn(mockUser);
        when(classeClient.findById(5L)).thenReturn(mockClasse);

        // WHEN
        List<AssistanceResponseDto> result = service.findAll();

        // THEN
        assertNotNull(result);
        assertEquals(1, result.size());
        assertNotNull(result.getFirst().getUser());
        assertNotNull(result.getFirst().getClasse());
    }

    @Test
    void givenEnrichmentFails_whenFindById_thenReturnDtoWithNulls() {
        // GIVEN
        when(repository.findById(1L)).thenReturn(Optional.of(assistanceEntity));
        // Simulamos caídas en los microservicios externos para forzar los bloques catch del mapeador
        when(userClient.findById(10L)).thenThrow(new RuntimeException("MS Usuarios Caído"));
        when(classeClient.findById(5L)).thenThrow(new RuntimeException("MS Classes Caído"));

        // WHEN
        AssistanceResponseDto result = service.findById(1L);

        // THEN
        assertNotNull(result);
        assertNull(result.getUser());
        assertNull(result.getClasse());
    }

    @Test
    void givenNonExistingId_whenFindById_thenReturnNull() {
        // GIVEN
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // WHEN
        AssistanceResponseDto result = service.findById(99L);

        // THEN
        assertNull(result);
    }

    // ==========================================
    // PRUEBAS: create() (Flujo Feliz y Errores de Validación)
    // ==========================================
    @Test
    void givenValidRequest_whenCreate_thenReturnCreatedDto() {
        // GIVEN
        when(userClient.findById(10L)).thenReturn(mockUser);
        when(classeClient.findById(5L)).thenReturn(mockClasse);
        when(repository.save(any(Assistance.class))).thenReturn(assistanceEntity);

        // WHEN
        AssistanceResponseDto result = service.create(requestDto);

        // THEN
        assertNotNull(result);
        assertEquals(1L, result.getIdAssistance());
        verify(repository, times(1)).save(any(Assistance.class));
    }

    @Test
    void givenInvalidUser_whenCreate_throwIllegalArgumentException() {
        // GIVEN
        when(userClient.findById(10L)).thenReturn(null); // El usuario no existe remótamente

        // WHEN & THEN
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            service.create(requestDto);
        });

        assertTrue(exception.getMessage().contains("El usuario con ID 10 no existe"));
        verify(repository, never()).save(any(Assistance.class));
    }

    @Test
    void givenInvalidClasse_whenCreate_throwIllegalArgumentException() {
        // GIVEN
        when(userClient.findById(10L)).thenReturn(mockUser);
        when(classeClient.findById(5L)).thenReturn(null); // La clase no existe remótamente

        // WHEN & THEN
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            service.create(requestDto);
        });

        assertTrue(exception.getMessage().contains("La clase con ID 5 no existe"));
    }

    // ==========================================
    // PRUEBAS: update()
    // ==========================================
    @Test
    void givenExistingIdAndValidRequest_whenUpdate_thenReturnUpdatedDto() {
        // GIVEN
        when(userClient.findById(10L)).thenReturn(mockUser);
        when(classeClient.findById(5L)).thenReturn(mockClasse);
        when(repository.existsById(1L)).thenReturn(true);
        when(repository.save(any(Assistance.class))).thenReturn(assistanceEntity);

        // WHEN
        AssistanceResponseDto result = service.update(1L, requestDto);

        // THEN
        assertNotNull(result);
        verify(repository, times(1)).save(any(Assistance.class));
    }

    @Test
    void givenNonExistingId_whenUpdate_thenReturnNull() {
        // GIVEN
        when(userClient.findById(10L)).thenReturn(mockUser);
        when(classeClient.findById(5L)).thenReturn(mockClasse);
        when(repository.existsById(99L)).thenReturn(false);

        // WHEN
        AssistanceResponseDto result = service.update(99L, requestDto);

        // THEN
        assertNull(result);
    }

    // ==========================================
    // PRUEBAS: delete()
    // ==========================================
    @Test
    void givenExistingId_whenDelete_thenReturnTrue() {
        // GIVEN
        when(repository.existsById(1L)).thenReturn(true);
        doNothing().when(repository).deleteById(1L);

        // WHEN
        boolean result = service.delete(1L);

        // THEN
        assertTrue(result);
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void givenNonExistingId_whenDelete_thenReturnFalse() {
        // GIVEN
        when(repository.existsById(99L)).thenReturn(false);

        // WHEN
        boolean result = service.delete(99L);

        // THEN
        assertFalse(result);
    }

    // ==========================================
    // PRUEBAS: findByUserId()
    // ==========================================
    @Test
    void givenExistingUser_whenFindByUserId_thenReturnList() throws Exception {
        // GIVEN
        when(userClient.findById(10L)).thenReturn(mockUser);
        when(repository.findByIdUser(10L)).thenReturn(List.of(assistanceEntity));
        when(classeClient.findById(5L)).thenReturn(mockClasse);

        // WHEN
        List<AssistanceResponseDto> result = service.findByUserId(10L);

        // THEN
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void givenNonExistingUser_whenFindByUserId_thenReturnNull() throws Exception {
        // GIVEN
        when(userClient.findById(99L)).thenReturn(null);

        // WHEN
        List<AssistanceResponseDto> result = service.findByUserId(99L);

        // THEN
        assertNull(result);
    }

    @Test
    void givenException_whenFindByUserId_throwException() {
        // GIVEN
        when(userClient.findById(10L)).thenThrow(new RuntimeException("Error en conexión"));

        // WHEN & THEN
        Exception exception = assertThrows(Exception.class, () -> {
            service.findByUserId(10L);
        });

        assertTrue(exception.getMessage().contains("Error en conexión"));
    }
}
