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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
        // Inicializamos los datos del entrenador para la Ciudad Deportiva
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

    // ==========================================
    // PRUEBAS: findAll()
    // ==========================================
    @Test
    void givenExistingCoaches_whenFindAll_thenReturnList() {
        // GIVEN
        when(repository.findAll()).thenReturn(List.of(coachEntity));

        // WHEN
        List<CoachResponseDto> result = service.findAll();

        // THEN
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Marcelo", result.get(0).getName());
        verify(repository, times(1)).findAll();
    }

    // ==========================================
    // PRUEBAS: findById()
    // ==========================================
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
        verify(repository, times(1)).findById(1L);
    }

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

    // ==========================================
    // PRUEBAS: create()
    // ==========================================
    @Test
    void givenValidRequest_whenCreate_thenReturnCreatedDto() {
        // GIVEN
        when(repository.save(any(Coach.class))).thenReturn(coachEntity);

        // WHEN
        CoachResponseDto result = service.create(requestDto);

        // THEN
        assertNotNull(result);
        assertEquals(1L, result.getIdCoach());
        assertEquals("Licencia PRO CONMEBOL", result.getCertification());
        verify(repository, times(1)).save(any(Coach.class));
    }

    // ==========================================
    // PRUEBAS: update()
    // ==========================================
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
        verify(repository, times(1)).existsById(1L);
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
        verify(repository, times(1)).existsById(99L);
        verify(repository, never()).deleteById(99L);
    }
}
