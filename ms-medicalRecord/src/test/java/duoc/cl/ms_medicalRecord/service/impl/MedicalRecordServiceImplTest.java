package duoc.cl.ms_medicalRecord.service.impl;

import duoc.cl.ms_medicalRecord.dto.MedicalRecordRequestDto;
import duoc.cl.ms_medicalRecord.dto.MedicalRecordResponseDto;
import duoc.cl.ms_medicalRecord.dto.UserResponseDto;
import duoc.cl.ms_medicalRecord.model.MedicalRecord;
import duoc.cl.ms_medicalRecord.repository.MedicalRecordRepository;
import duoc.cl.ms_medicalRecord.service.api.UserClient;
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
class MedicalRecordServiceImplTest {

    @Mock
    private MedicalRecordRepository repository;

    @Mock
    private UserClient userClient; // Simulamos la llamada Feign para no depender del otro microservicio activo

    @InjectMocks
    private MedicalRecordServiceImpl service;

    private MedicalRecord recordEntity;
    private MedicalRecordRequestDto requestDto;
    private UserResponseDto mockUserResponse;

    @BeforeEach
    void setUp() {
        recordEntity = new MedicalRecord(1L, "Penicilina", "Asma", "Centro Deportivo Central", 10L);
        requestDto = new MedicalRecordRequestDto(1L, "Penicilina", "Asma", "Centro Deportivo Central", 10L);

        // Mock de la respuesta simulada del microservicio de usuarios
        mockUserResponse = new UserResponseDto();
    }

    // ==========================================
    // PRUEBAS: findAll()
    // ==========================================
    @Test
    void givenExistingRecords_whenFindAll_thenReturnList() {
        when(repository.findAll()).thenReturn(List.of(recordEntity));

        List<MedicalRecordResponseDto> result = service.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Penicilina", result.get(0).getAllergy());
        verify(repository, times(1)).findAll();
    }

    // ==========================================
    // PRUEBAS: findById()
    // ==========================================
    @Test
    void givenExistingId_whenFindById_thenReturnDto() {
        when(repository.findById(1L)).thenReturn(Optional.of(recordEntity));

        MedicalRecordResponseDto result = service.findById(1L);

        assertNotNull(result);
        assertEquals("Asma", result.getDisease());
    }

    @Test
    void givenNonExistingId_whenFindById_thenReturnNull() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        MedicalRecordResponseDto result = service.findById(99L);

        assertNull(result);
    }

    // ==========================================
    // PRUEBAS: findByUserId() (Feign Client - Crítico)
    // ==========================================
    @Test
    void givenExistingUserInFeign_whenFindByUserId_thenReturnList() throws Exception {
        // GIVEN: El cliente Feign encuentra al usuario y la BD tiene sus fichas médicas
        when(userClient.findById(10L)).thenReturn(mockUserResponse);
        when(repository.findByUserId(10L)).thenReturn(List.of(recordEntity));

        // WHEN
        List<MedicalRecordResponseDto> result = service.findByUserId(10L);

        // THEN
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(userClient, times(1)).findById(10L);
    }

    @Test
    void givenNonExistingUserInFeign_whenFindByUserId_thenReturnNull() throws Exception {
        // GIVEN: Feign retorna null (usuario no existe en el sistema)
        when(userClient.findById(99L)).thenReturn(null);

        // WHEN
        List<MedicalRecordResponseDto> result = service.findByUserId(99L);

        // THEN
        assertNull(result);
        verify(repository, never()).findByUserId(anyLong()); // Validamos que ni siquiera consulte a la BD
    }

    @Test
    void givenFeignException_whenFindByUserId_throwException() {
        // GIVEN: Simulamos una caída de red o error de timeout en Feign Client
        when(userClient.findById(10L)).thenThrow(new RuntimeException("Timeout Connection"));

        // WHEN & THEN
        Exception exception = assertThrows(Exception.class, () -> {
            service.findByUserId(10L);
        });

        assertTrue(exception.getMessage().contains("Timeout Connection"));
    }

    // ==========================================
    // PRUEBAS: create() y update()
    // ==========================================
    @Test
    void givenValidRequest_whenCreate_thenReturnCreatedDto() {
        when(repository.save(any(MedicalRecord.class))).thenReturn(recordEntity);

        MedicalRecordResponseDto result = service.create(requestDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void givenExistingId_whenUpdate_thenReturnUpdatedDto() {
        when(repository.existsById(1L)).thenReturn(true);
        when(repository.save(any(MedicalRecord.class))).thenReturn(recordEntity);

        MedicalRecordResponseDto result = service.update(1L, requestDto);

        assertNotNull(result);
        verify(repository, times(1)).save(any(MedicalRecord.class));
    }

    @Test
    void givenNonExistingId_whenUpdate_thenReturnNull() {
        when(repository.existsById(99L)).thenReturn(false);

        MedicalRecordResponseDto result = service.update(99L, requestDto);

        assertNull(result);
        verify(repository, never()).save(any(MedicalRecord.class));
    }

    // ==========================================
    // PRUEBAS: deleteById()
    // ==========================================
    @Test
    void givenExistingId_whenDeleteById_thenReturnTrue() {
        when(repository.existsById(1L)).thenReturn(true);
        doNothing().when(repository).deleteById(1L);

        boolean result = service.deleteById(1L);

        assertTrue(result);
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void givenNonExistingId_whenDeleteById_thenReturnFalse() {
        when(repository.existsById(99L)).thenReturn(false);

        boolean result = service.deleteById(99L);

        assertFalse(result);
        verify(repository, never()).deleteById(anyLong());
    }
}
