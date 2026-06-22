package duoc.cl.ms_classes.service.impl;

import duoc.cl.ms_classes.dto.ClasseRequestDto;
import duoc.cl.ms_classes.dto.ClasseResponseDto;
import duoc.cl.ms_classes.model.Classe;
import duoc.cl.ms_classes.model.TypeClasses;
import duoc.cl.ms_classes.repository.ClasseRepository;
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
class ClasseServiceImplTest {

    @Mock
    private ClasseRepository repository;

    @InjectMocks
    private ClasseServiceImpl service;

    private Classe classeEntity;
    private ClasseRequestDto requestDto;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @BeforeEach
    void setUp() {
        startDate = LocalDateTime.of(2026, 6, 22, 19, 0);
        endDate = LocalDateTime.of(2026, 6, 22, 20, 30);
        TypeClasses typeClasse = new TypeClasses(1L, "Crossfit");

        classeEntity = new Classe(1L, startDate, endDate, 20, 15, typeClasse);
        requestDto = new ClasseRequestDto(1L, startDate, endDate, 20, 15, typeClasse);
    }

    @Test
    void givenExistingClasses_whenFindAll_thenReturnList() {
        when(repository.findAll()).thenReturn(List.of(classeEntity));
        List<ClasseResponseDto> result = service.findAll();
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void givenExistingId_whenFindById_thenReturnResponseDto() {
        when(repository.findById(1L)).thenReturn(Optional.of(classeEntity));
        ClasseResponseDto result = service.findById(1L);
        assertNotNull(result);
        assertEquals(15, result.getSpotsAvailable());
    }

    @Test
    void givenNonExistingId_whenFindById_thenReturnNull() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        ClasseResponseDto result = service.findById(99L);
        assertNull(result);
    }

    @Test
    void givenValidRequest_whenCreate_thenReturnCreatedResponseDto() {
        when(repository.save(any(Classe.class))).thenReturn(classeEntity);
        ClasseResponseDto result = service.create(requestDto);
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void givenExistingIdAndValidRequest_whenUpdate_thenReturnUpdatedResponseDto() {
        when(repository.existsById(1L)).thenReturn(true);
        when(repository.save(any(Classe.class))).thenReturn(classeEntity);
        ClasseResponseDto result = service.update(1L, requestDto);
        assertNotNull(result);
        verify(repository, times(1)).save(any(Classe.class));
    }

    @Test
    void givenNonExistingId_whenUpdate_thenReturnNull() {
        when(repository.existsById(99L)).thenReturn(false);
        ClasseResponseDto result = service.update(99L, requestDto);
        assertNull(result);
        verify(repository, never()).save(any(Classe.class));
    }

    @Test
    void givenExistingId_whenDeleteById_thenReturnTrue() {
        when(repository.existsById(1L)).thenReturn(true);
        doNothing().when(repository).deleteById(1L);
        boolean result = service.deleteById(1L);
        assertTrue(result);
    }

    @Test
    void givenNonExistingId_whenDeleteById_thenReturnFalse() {
        when(repository.existsById(99L)).thenReturn(false);
        boolean result = service.deleteById(99L);
        assertFalse(result);
        verify(repository, never()).deleteById(99L);
    }
}
