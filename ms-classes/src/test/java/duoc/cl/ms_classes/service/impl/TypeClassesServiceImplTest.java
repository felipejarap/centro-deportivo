package duoc.cl.ms_classes.service.impl;

import duoc.cl.ms_classes.dto.TypeClassesRequestDto;
import duoc.cl.ms_classes.dto.TypeClassesResponseDto;
import duoc.cl.ms_classes.model.TypeClasses;
import duoc.cl.ms_classes.repository.TypeClassesRepository;
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
class TypeClassesServiceImplTest {

    @Mock
    private TypeClassesRepository repository;

    @InjectMocks
    private TypeClassesServiceImpl service;

    private TypeClasses typeEntity;
    private TypeClassesRequestDto requestDto;

    @BeforeEach
    void setUp() {
        typeEntity = new TypeClasses(1L, "Natacion");
        requestDto = new TypeClassesRequestDto(1L, "Natacion");
    }

    @Test
    void givenExistingRecords_whenFindAll_thenReturnList() {
        when(repository.findAll()).thenReturn(List.of(typeEntity));
        List<TypeClassesResponseDto> result = service.findAll();
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void givenExistingId_whenFindById_thenReturnResponseDto() {
        when(repository.findById(1L)).thenReturn(Optional.of(typeEntity));
        TypeClassesResponseDto result = service.findById(1L);
        assertNotNull(result);
        assertEquals("Natacion", result.getName());
    }

    @Test
    void givenNonExistingId_whenFindById_thenReturnNull() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        TypeClassesResponseDto result = service.findById(99L);
        assertNull(result);
    }

    @Test
    void givenValidRequest_whenCreate_thenReturnCreatedResponseDto() {
        when(repository.save(any(TypeClasses.class))).thenReturn(typeEntity);
        TypeClassesResponseDto result = service.create(requestDto);
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void givenExistingIdAndValidRequest_whenUpdate_thenReturnUpdatedResponseDto() {
        when(repository.existsById(1L)).thenReturn(true);
        when(repository.save(any(TypeClasses.class))).thenReturn(typeEntity);
        TypeClassesResponseDto result = service.update(1L, requestDto);
        assertNotNull(result);
        verify(repository, times(1)).save(any(TypeClasses.class));
    }

    @Test
    void givenNonExistingId_whenUpdate_thenReturnNull() {
        when(repository.existsById(99L)).thenReturn(false);
        TypeClassesResponseDto result = service.update(99L, requestDto);
        assertNull(result);
        verify(repository, never()).save(any(TypeClasses.class));
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
