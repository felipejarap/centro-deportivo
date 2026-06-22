package cl.duoc.ms_rutinaEjercicio.service.impl;

import cl.duoc.ms_rutinaEjercicio.dto.CoachResponseDto;
import cl.duoc.ms_rutinaEjercicio.dto.ExerciseRoutineRequestDto;
import cl.duoc.ms_rutinaEjercicio.dto.ExerciseRoutineResponseDto;
import cl.duoc.ms_rutinaEjercicio.dto.UserResponseDto;
import cl.duoc.ms_rutinaEjercicio.model.ExerciseRoutine;
import cl.duoc.ms_rutinaEjercicio.respository.ExerciseRoutineRepository;
import cl.duoc.ms_rutinaEjercicio.service.api.CoachClient;
import cl.duoc.ms_rutinaEjercicio.service.api.UserClient;
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
class ExerciseRoutineServiceImplTest {

    @Mock
    private ExerciseRoutineRepository repository;

    @Mock
    private UserClient userClient;

    @Mock
    private CoachClient coachClient;

    @InjectMocks
    private ExerciseRoutineServiceImpl service;

    private ExerciseRoutine routineEntity;
    private ExerciseRoutineRequestDto requestDto;
    private UserResponseDto mockUser;
    private CoachResponseDto mockCoach;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();

        routineEntity = new ExerciseRoutine();
        routineEntity.setIdRoutine(1L);
        routineEntity.setIdUser(10L);
        routineEntity.setIdCoach(5L);
        routineEntity.setName("Rutina Hipertrofia");
        routineEntity.setDescription("Rutina enfocada en tren superior");
        routineEntity.setObjective("Ganancia muscular");
        routineEntity.setRecordedWeight(75.5);
        routineEntity.setPersonalBrand(80.6);
        routineEntity.setAssignmentDate(now);
        routineEntity.setActive(true);

        requestDto = new ExerciseRoutineRequestDto();
        requestDto.setIdUser(10L);
        requestDto.setIdCoach(5L);
        requestDto.setName("Rutina Hipertrofia");
        requestDto.setDescription("Rutina enfocada en tren superior");
        requestDto.setObjective("Ganancia muscular");
        requestDto.setRecordedWeight(75.5);
        requestDto.setPersonalBrand(80.9);
        requestDto.setAssignmentDate(now);
        requestDto.setActive(true);

        mockUser = new UserResponseDto();
        mockUser.setId(10L);
        mockUser.setUsername("felipe.perez");

        mockCoach = new CoachResponseDto();
        mockCoach.setId(5L);
        mockCoach.setName("Marcelo");
    }

    // ==========================================
    // PRUEBAS: findAll() y findById()
    // ==========================================
    @Test
    void givenExistingRoutines_whenFindAll_thenReturnEnrichedList() {
        when(repository.findAll()).thenReturn(List.of(routineEntity));
        when(userClient.findById(10L)).thenReturn(mockUser);
        when(coachClient.findById(5L)).thenReturn(mockCoach);

        List<ExerciseRoutineResponseDto> result = service.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertNotNull(result.get(0).getUsers());
        assertNotNull(result.get(0).getCoaches());
    }

    @Test
    void givenFeignClientsFail_whenFindById_thenReturnDtoWithNulls() {
        when(repository.findById(1L)).thenReturn(Optional.of(routineEntity));
        // Forzamos los bloques catch simulando caídas de red
        when(userClient.findById(10L)).thenThrow(new RuntimeException("Error UserClient"));
        when(coachClient.findById(5L)).thenThrow(new RuntimeException("Error CoachClient"));

        ExerciseRoutineResponseDto result = service.findById(1L);

        assertNotNull(result);
        assertNull(result.getUsers());
        assertNull(result.getCoaches());
    }

    @Test
    void givenNonExistingId_whenFindById_thenReturnNull() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        ExerciseRoutineResponseDto result = service.findById(99L);
        assertNull(result);
    }

    // ==========================================
    // PRUEBAS: create()
    // ==========================================
    @Test
    void givenValidRequest_whenCreate_thenReturnCreatedDto() {
        when(userClient.findById(10L)).thenReturn(mockUser);
        when(coachClient.findById(5L)).thenReturn(mockCoach);
        when(repository.save(any(ExerciseRoutine.class))).thenReturn(routineEntity);

        ExerciseRoutineResponseDto result = service.create(requestDto);

        assertNotNull(result);
        assertEquals(1L, result.getIdRoutine());
        verify(repository, times(1)).save(any(ExerciseRoutine.class));
    }

    @Test
    void givenInvalidUser_whenCreate_throwIllegalArgumentException() {
        when(userClient.findById(10L)).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            service.create(requestDto);
        });

        assertTrue(exception.getMessage().contains("El usuario con ID 10 no existe"));
        verify(repository, never()).save(any(ExerciseRoutine.class));
    }

    @Test
    void givenInvalidCoach_whenCreate_throwIllegalArgumentException() {
        when(userClient.findById(10L)).thenReturn(mockUser);
        when(coachClient.findById(5L)).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            service.create(requestDto);
        });

        assertTrue(exception.getMessage().contains("El Entrenador con ID 5 no existe"));
    }

    // ==========================================
    // PRUEBAS: update()
    // ==========================================
    @Test
    void givenExistingIdAndValidRequest_whenUpdate_thenReturnUpdatedDto() {
        when(userClient.findById(10L)).thenReturn(mockUser);
        when(coachClient.findById(5L)).thenReturn(mockCoach);
        when(repository.existsById(1L)).thenReturn(true);
        when(repository.save(any(ExerciseRoutine.class))).thenReturn(routineEntity);

        ExerciseRoutineResponseDto result = service.update(1L, requestDto);

        assertNotNull(result);
        verify(repository, times(1)).save(any(ExerciseRoutine.class));
    }

    @Test
    void givenNonExistingId_whenUpdate_thenReturnNull() {
        when(userClient.findById(10L)).thenReturn(mockUser);
        when(coachClient.findById(5L)).thenReturn(mockCoach);
        when(repository.existsById(99L)).thenReturn(false);

        ExerciseRoutineResponseDto result = service.update(99L, requestDto);

        assertNull(result);
    }

    // ==========================================
    // PRUEBAS: delete()
    // ==========================================
    @Test
    void givenExistingId_whenDelete_thenReturnTrue() {
        when(repository.existsById(1L)).thenReturn(true);
        doNothing().when(repository).deleteById(1L);

        boolean result = service.delete(1L);

        assertTrue(result);
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void givenNonExistingId_whenDelete_thenReturnFalse() {
        // GIVEN: El repositorio confirma que la rutina con ID 99 no existe
        when(repository.existsById(99L)).thenReturn(false);

        // WHEN: Ejecutamos el método delete de tu servicio
        boolean result = service.delete(99L);

        // THEN: Validamos que retorne false y que jamás intente borrar nada
        assertFalse(result);
        verify(repository, times(1)).existsById(99L);
        verify(repository, never()).deleteById(anyLong());
    }

    // ==========================================
    // PRUEBAS: findByUserId()
    // ==========================================
    @Test
    void givenExistingUser_whenFindByUserId_thenReturnNull() throws Exception {
        when(userClient.findById(10L)).thenReturn(null);

        List<ExerciseRoutineResponseDto> result = service.findByUserId(10L);

        assertNull(result);
    }

    @Test
    void givenExistingUserInFeign_whenFindByUserId_thenReturnList() throws Exception {
        // GIVEN: El cliente Feign sí encuentra al usuario y el repositorio tiene rutinas asignadas
        when(userClient.findById(10L)).thenReturn(mockUser);
        when(repository.findByIdUser(10L)).thenReturn(List.of(routineEntity));
        when(coachClient.findById(5L)).thenReturn(mockCoach); // Para el mapeo interno (toDto)

        // WHEN
        List<ExerciseRoutineResponseDto> result = service.findByUserId(10L);

        // THEN
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(repository, times(1)).findByIdUser(10L);
    }

    @Test
    void givenUserClientException_whenFindByUserId_throwException() {
        // GIVEN: Simulamos una caída de red o timeout en el bloque try-catch de findByUserId
        when(userClient.findById(10L)).thenThrow(new RuntimeException("Error de conexion remota"));

        // WHEN & THEN: Validamos el bloque catch que faltaba
        Exception exception = assertThrows(Exception.class, () -> {
            service.findByUserId(10L);
        });

        assertNotNull(exception);
    }





}
