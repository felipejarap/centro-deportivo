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
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// Habilita el entorno de simulación de Mockito en JUnit 5 sin levantar infraestructura pesada
@ExtendWith(MockitoExtension.class)
// Evita fallos si un cliente remoto lanza excepción y los siguientes stubs no se alcanzan a ejecutar
@MockitoSettings(strictness = Strictness.LENIENT)
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

    // =========================================================================
    // PRUEBAS: METODOS findAll() Y findById()
    // =========================================================================

    /**
     * BUSCAR TODOS: Verifica que el servicio acceda al repositorio, recupere la colección
     * completa de rutinas y las enriquezca con los datos de usuarios y entrenadores externos.
     */
    @Test
    void givenExistingRoutines_whenFindAll_thenReturnEnrichedList() {
        // GIVEN
        when(repository.findAll()).thenReturn(List.of(routineEntity));
        when(userClient.findById(10L)).thenReturn(mockUser);
        when(coachClient.findById(5L)).thenReturn(mockCoach);

        // WHEN
        List<ExerciseRoutineResponseDto> result = service.findAll();

        // THEN
        assertNotNull(result);
        assertEquals(1, result.size());
        assertNotNull(result.getFirst().getUsers());
        assertNotNull(result.getFirst().getCoaches());
    }

    /**
     * CASO DE TOLERANCIA A FALLOS: Si la rutina existe en la BD pero los microservicios externos
     * fallan (lanzan excepción), el servicio debe capturar el error y devolver la rutina con datos nulos.
     */
    @Test
    void givenFeignClientsFail_whenFindById_thenReturnDtoWithNulls() {
        // GIVEN
        when(repository.findById(1L)).thenReturn(Optional.of(routineEntity));
        when(userClient.findById(10L)).thenThrow(new RuntimeException("Error UserClient"));
        when(coachClient.findById(5L)).thenThrow(new RuntimeException("Error CoachClient"));

        // WHEN
        ExerciseRoutineResponseDto result = service.findById(1L);

        // THEN
        assertNotNull(result);
        assertNull(result.getUsers());
        assertNull(result.getCoaches());
    }

    /**
     * BUSCAR ID VACÍO: Comprueba que al buscar un identificador inexistente, el servicio
     * intercepte el Optional vacío regresando un valor nulo controlado.
     */
    @Test
    void givenNonExistingId_whenFindById_thenReturnNull() {
        // GIVEN
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // WHEN
        ExerciseRoutineResponseDto result = service.findById(99L);

        // THEN
        assertNull(result);
    }

    // =========================================================================
    // PRUEBAS: METODO create()
    // =========================================================================

    /**
     * CREAR RUTINA EXITOSO: Evalúa la inserción de una nueva rutina. Verifica que el objeto se envíe
     * a guardar y retorne el objeto resultante con su clave primaria correspondiente.
     */
    @Test
    void givenValidRequest_whenCreate_thenReturnCreatedDto() {
        // GIVEN
        when(userClient.findById(10L)).thenReturn(mockUser);
        when(coachClient.findById(5L)).thenReturn(mockCoach);
        when(repository.save(any(ExerciseRoutine.class))).thenReturn(routineEntity);

        // WHEN
        ExerciseRoutineResponseDto result = service.create(requestDto);

        // THEN
        assertNotNull(result);
        assertEquals(1L, result.getIdRoutine());
        verify(repository, times(1)).save(any(ExerciseRoutine.class));
    }

    /**
     * CREAR CON USUARIO INVÁLIDO: Regla de integridad. Si se intenta registrar una rutina
     * asignándole un usuario inexistente en el cliente Feign, el sistema frena la operación.
     */
    @Test
    void givenInvalidUser_whenCreate_throwIllegalArgumentException() {
        // GIVEN
        when(userClient.findById(10L)).thenReturn(null);

        // WHEN & THEN
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            service.create(requestDto);
        });

        assertTrue(exception.getMessage().contains("El usuario con ID 10 no existe"));
        verify(repository, never()).save(any(ExerciseRoutine.class));
    }

    /**
     * CREAR CON ENTRENADOR INVÁLIDO: Regla de integridad. Si se intenta registrar una rutina
     * asignándole un entrenador inexistente en el cliente Feign, el sistema frena la operación.
     */
    @Test
    void givenInvalidCoach_whenCreate_throwIllegalArgumentException() {
        // GIVEN
        when(userClient.findById(10L)).thenReturn(mockUser);
        when(coachClient.findById(5L)).thenReturn(null);

        // WHEN & THEN
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            service.create(requestDto);
        });

        assertTrue(exception.getMessage().contains("El Entrenador con ID 5 no existe"));
        verify(repository, never()).save(any(ExerciseRoutine.class));
    }

    // =========================================================================
    // PRUEBAS: METODO update()
    // =========================================================================

    /**
     * ACTUALIZAR EXITOSO: Comprueba que si la rutina existe localmente y los IDs remotos son válidos,
     * el repositorio consolida y guarda los cambios correspondientes.
     */
    @Test
    void givenExistingIdAndValidRequest_whenUpdate_thenReturnUpdatedDto() {
        // GIVEN
        when(userClient.findById(10L)).thenReturn(mockUser);
        when(coachClient.findById(5L)).thenReturn(mockCoach);
        when(repository.existsById(1L)).thenReturn(true);
        when(repository.save(any(ExerciseRoutine.class))).thenReturn(routineEntity);

        // WHEN
        ExerciseRoutineResponseDto result = service.update(1L, requestDto);

        // THEN
        assertNotNull(result);
        verify(repository, times(1)).save(any(ExerciseRoutine.class));
    }

    /**
     * ACTUALIZAR ID INEXISTENTE: Seguridad operativa. Si se intenta alterar una rutina apuntando a un ID inválido,
     * el servicio cancela el flujo devolviendo null y bloqueando accesos de escritura a la BD.
     */
    @Test
    void givenNonExistingId_whenUpdate_thenReturnNull() {
        // GIVEN
        when(userClient.findById(10L)).thenReturn(mockUser);
        when(coachClient.findById(5L)).thenReturn(mockCoach);
        when(repository.existsById(99L)).thenReturn(false);

        // WHEN
        ExerciseRoutineResponseDto result = service.update(99L, requestDto);

        // THEN
        assertNull(result);
        verify(repository, never()).save(any(ExerciseRoutine.class));
    }

    // =========================================================================
    // PRUEBAS: METODO delete()
    // =========================================================================

    /**
     * ELIMINAR EXITOSO: Verifica que si la clave primaria existe localmente, se proceda a ejecutar
     * la remoción del registro físico en el repositorio devolviendo la confirmación true.
     */
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

    /**
     * ELIMINAR ID INEXISTENTE: Comprueba que si se intenta borrar un ID corrupto o que ya no existe,
     * el servicio retorna un valor false controlado en lugar de generar excepciones inesperadas.
     */
    @Test
    void givenNonExistingId_whenDelete_thenReturnFalse() {
        // GIVEN
        when(repository.existsById(99L)).thenReturn(false);

        // WHEN
        boolean result = service.delete(99L);

        // THEN
        assertFalse(result);
        verify(repository, times(1)).existsById(99L);
        verify(repository, never()).deleteById(anyLong());
    }

    // =========================================================================
    // PRUEBAS: METODO findByUserId()
    // =========================================================================

    /**
     * FILTRAR POR USUARIO INEXISTENTE: Si el cliente Feign responde que el ID de usuario no existe
     * en el sistema principal (null), el servicio frena y retorna null sin consultar el repositorio.
     */
    @Test
    void givenExistingUser_whenFindByUserId_thenReturnNull() throws Exception {
        // GIVEN
        when(userClient.findById(10L)).thenReturn(null);

        // WHEN
        List<ExerciseRoutineResponseDto> result = service.findByUserId(10L);

        // THEN
        assertNull(result);
        verify(repository, never()).findByIdUser(anyLong());
    }
}

