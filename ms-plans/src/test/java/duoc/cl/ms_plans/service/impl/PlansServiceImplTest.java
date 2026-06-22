package duoc.cl.ms_plans.service.impl;

import duoc.cl.ms_plans.dto.PlansRequestDto;
import duoc.cl.ms_plans.dto.PlansResponseDto;
import duoc.cl.ms_plans.model.Plans;
import duoc.cl.ms_plans.repository.PlansRepository;
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
class PlansServiceImplTest {

    @Mock
    private PlansRepository repository;

    @InjectMocks
    private PlansServiceImpl service;

    private Plans plansEntity;
    private PlansRequestDto requestDto;

    @BeforeEach
    void setUp() {
        // Inicializamos objetos de prueba coherentes con la ciudad deportiva (ej: "Plan Mensual Full")
        plansEntity = new Plans(1L, "Plan Mensual Full", 35000, 30);
        requestDto = new PlansRequestDto(1L, "Plan Mensual Full", 35000, 30);
    }

    // ==========================================
    // PRUEBAS: findAll()
    // ==========================================
    @Test
    void givenExistingPlans_whenFindAll_thenReturnList() {
        // GIVEN
        when(repository.findAll()).thenReturn(List.of(plansEntity));

        // WHEN
        List<PlansResponseDto> result = service.findAll();

        // THEN
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Plan Mensual Full", result.get(0).getName());
        verify(repository, times(1)).findAll();
    }

    // ==========================================
    // PRUEBAS: findById()
    // ==========================================
    @Test
    void givenExistingId_whenFindById_thenReturnDto() {
        // GIVEN
        when(repository.findById(1L)).thenReturn(Optional.of(plansEntity));

        // WHEN
        PlansResponseDto result = service.findById(1L);

        // THEN
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(35000, result.getPrice());
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void givenNonExistingId_whenFindById_thenReturnNull() {
        // GIVEN
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // WHEN
        PlansResponseDto result = service.findById(99L);

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
        when(repository.save(any(Plans.class))).thenReturn(plansEntity);

        // WHEN
        PlansResponseDto result = service.create(requestDto);

        // THEN
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(30, result.getDurationDays());
        verify(repository, times(1)).save(any(Plans.class));
    }

    // ==========================================
    // PRUEBAS: update()
    // ==========================================
    @Test
    void givenExistingIdAndValidRequest_whenUpdate_thenReturnUpdatedDto() {
        // GIVEN
        when(repository.existsById(1L)).thenReturn(true);
        when(repository.save(any(Plans.class))).thenReturn(plansEntity);

        // WHEN
        PlansResponseDto result = service.update(1L, requestDto);

        // THEN
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(repository, times(1)).existsById(1L);
        verify(repository, times(1)).save(any(Plans.class));
    }

    @Test
    void givenNonExistingId_whenUpdate_thenReturnNull() {
        // GIVEN
        when(repository.existsById(99L)).thenReturn(false);

        // WHEN
        PlansResponseDto result = service.update(99L, requestDto);

        // THEN
        assertNull(result);
        verify(repository, times(1)).existsById(99L);
        verify(repository, never()).save(any(Plans.class));
    }

    // ==========================================
    // PRUEBAS: deleteById()
    // ==========================================
    @Test
    void givenExistingId_whenDeleteById_thenReturnTrue() {
        // GIVEN
        when(repository.existsById(1L)).thenReturn(true);
        doNothing().when(repository).deleteById(1L);

        // WHEN
        boolean result = service.deleteById(1L);

        // THEN
        assertTrue(result);
        verify(repository, times(1)).existsById(1L);
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void givenNonExistingId_whenDeleteById_thenReturnFalse() {
        // GIVEN
        when(repository.existsById(99L)).thenReturn(false);

        // WHEN
        boolean result = service.deleteById(99L);

        // THEN
        assertFalse(result);
        verify(repository, times(1)).existsById(99L);
        verify(repository, never()).deleteById(99L);
    }
}
