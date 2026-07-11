package cl.Duoc.MS_Entrenadores.service.impl;

import cl.Duoc.MS_Entrenadores.dto.CoachRequestDto;
import cl.Duoc.MS_Entrenadores.dto.CoachResponseDto;
import cl.Duoc.MS_Entrenadores.model.Coach;
import cl.Duoc.MS_Entrenadores.repository.CoachRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// Habilita el entorno de pruebas unitarias puras con Mockito en JUnit 5
@ExtendWith(MockitoExtension.class)
class CoachServiceImplTest {

    @Mock
    private CoachRepository repository;

    @InjectMocks
    private CoachServiceImpl service;

    private Coach coachEntity;
    private CoachRequestDto requestDto;

    @BeforeEach
    void setUp() {
        // Inicializamos los datos del entrenador ficticio
        coachEntity = new Coach();
        coachEntity.setIdCoach(1L);
        coachEntity.setName("Marcelo");
        coachEntity.setPaternalSurname("Bielsa");
        coachEntity.setMaternalSurname("Caldera");
        coachEntity.setSpecialty("Futbol");
        coachEntity.setCertification("Licencia PRO CONMEBOL");

        requestDto = new CoachRequestDto();
        requestDto.setName("Marcelo");
        requestDto.setPaternalSurname("Bielsa");
        requestDto.setMaternalSurname("Caldera");
        requestDto.setSpecialty("Futbol");
        requestDto.setCertification("Licencia PRO CONMEBOL");
    }

    // =========================================================================
    // PRUEBAS: METODO findAll()
    // =========================================================================

    /**
     * BUSCAR TODOS - CASO CON REGISTROS: Valida que al recuperar entrenadores se recorra el stream,
     * se ejecute el mapeo de campos de 'toDto()' y devuelva la lista correctamente.
     */
    @Test
    void givenExistingCoaches_whenFindAll_thenReturnList() {
        // GIVEN
        when(repository.findAll()).thenReturn(List.of(coachEntity));

        // WHEN
        List<CoachResponseDto> result = service.findAll();

        // THEN
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.getFirst().getIdCoach());
        assertEquals("Marcelo", result.getFirst().getName());
        verify(repository, times(1)).findAll();
    }

    /**
     * BUSCAR TODOS - CASO VACÍO: Garantiza la cobertura del stream cuando la lista local
     * viene sin elementos.
     */
    @Test
    void givenNoCoaches_whenFindAll_thenReturnEmptyList() {
        // GIVEN
        when(repository.findAll()).thenReturn(Collections.emptyList());

        // WHEN
        List<CoachResponseDto> result = service.findAll();

        // THEN
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(repository, times(1)).findAll();
    }

    // =========================================================================
    // PRUEBAS: METODO findById()
    // =========================================================================

    /**
     * BUSCAR POR ID EXISTENTE: Valida la ruta del '.map(this::toDto)' extrayendo todos los
     * campos del entrenador mapeados del objeto de persistencia.
     */
    @Test
    void givenExistingId_whenFindById_thenReturnDto() {
        // GIVEN
        when(repository.findById(1L)).thenReturn(Optional.of(coachEntity));

        // WHEN
        CoachResponseDto result = service.findById(1L);

        // THEN
        assertNotNull(result);
        assertEquals(1L, result.getIdCoach());
        assertEquals("Futbol", result.getSpecialty());
        assertEquals("Bielsa", result.getPaternalSurname());
        assertEquals("Caldera", result.getMaternalSurname());
        assertEquals("Licencia PRO CONMEBOL", result.getCertification());
        verify(repository, times(1)).findById(1L);
    }

    /**
     * BUSCAR POR ID INEXISTENTE: Garantiza la cobertura de la bifurcación '.orElse(null)' del Optional.
     */
    @Test
    void givenNonExistingId_whenFindById_thenReturnNull() {
        // GIVEN
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // WHEN
        CoachResponseDto result = service.findById(99L);

        // THEN
        assertNull(result);
        verify(repository, times(1)).findById(99L);
    }

    // =========================================================================
    // PRUEBAS: METODO create()
    // =========================================================================

    /**
     * CREAR ENTRENADOR: Cubre por completo las asignaciones internas del método privado 'toEntity()'
     * al procesar el DTO de entrada previo al guardado.
     */
    @Test
    void givenValidRequest_whenCreate_thenReturnCreatedDto() {
        // GIVEN
        when(repository.save(any(Coach.class))).thenReturn(coachEntity);

        // WHEN
        CoachResponseDto result = service.create(requestDto);

        // THEN
        assertNotNull(result);
        assertEquals(1L, result.getIdCoach());
        assertEquals("Marcelo", result.getName());
        verify(repository, times(1)).save(any(Coach.class));
    }

    // =========================================================================
    // PRUEBAS: METODO update()
    // =========================================================================

    /**
     * ACTUALIZAR ID EXISTENTE: Cubre el bloque 'if (repository.existsById(id))' ejecutando la reasignación
     * explícita del ID sobre el objeto antes de guardarlo en base de datos.
     */
    @Test
    void givenExistingIdAndValidRequest_whenUpdate_thenReturnUpdatedDto() {
        // GIVEN
        when(repository.existsById(1L)).thenReturn(true);
        when(repository.save(any(Coach.class))).thenReturn(coachEntity);

        // WHEN
        CoachResponseDto result = service.update(1L, requestDto);

        // THEN
        assertNotNull(result);
        assertEquals(1L, result.getIdCoach());
        verify(repository, times(1)).existsById(1L);
        verify(repository, times(1)).save(any(Coach.class));
    }

    /**
     * ACTUALIZAR ID INEXISTENTE: Cubre el retorno de la rama 'else' implícita del bloque condicional,
     * regresando un valor nulo controlado.
     */
    @Test
    void givenNonExistingId_whenUpdate_thenReturnNull() {
        // GIVEN
        when(repository.existsById(99L)).thenReturn(false);

        // WHEN
        CoachResponseDto result = service.update(99L, requestDto);

        // THEN
        assertNull(result);
        verify(repository, times(1)).existsById(99L);
        verify(repository, never()).save(any(Coach.class));
    }

    // =========================================================================
    // PRUEBAS: METODO delete()
    // =========================================================================

    /**
     * ELIMINAR ID EXISTENTE: Cubre la bifurcación positiva del borrado, garantizando que el retorno
     * sea 'true' tras invocar la sentencia en el repositorio.
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
        verify(repository, times(1)).existsById(1L);
        verify(repository, times(1)).deleteById(1L);
    }

    /**
     * ELIMINAR ID INEXISTENTE: Cubre la bifurcación alternativa del condicional retornando false
     * y anulando llamadas de ejecución hacia el repositorio de datos.
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
        verify(repository, never()).deleteById(99L);
    }
}
