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

// Habilita el entorno de pruebas unitarias puras con Mockito en JUnit 5
@ExtendWith(MockitoExtension.class)
class MedicalRecordServiceImplTest {

    // Simula el repositorio local de fichas médicas
    @Mock
    private MedicalRecordRepository repository;

    // Simulamos la llamada Feign para no depender del otro microservicio activo
    @Mock
    private UserClient userClient;

    // Inyecta automáticamente los mocks declarados arriba en la implementación del servicio
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

    // =========================================================================
    // PRUEBAS: METODO findAll()
    // =========================================================================

    /**
     * BUSCAR TODOS: Verifica que el servicio acceda al repositorio, recupere la colección
     * completa de fichas médicas y las transforme correctamente a una lista de DTOs.
     */
    @Test
    void givenExistingRecords_whenFindAll_thenReturnList() {
        // GIVEN
        when(repository.findAll()).thenReturn(List.of(recordEntity));

        // WHEN
        List<MedicalRecordResponseDto> result = service.findAll();

        // THEN
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Penicilina", result.getFirst().getAllergy());
        verify(repository, times(1)).findAll();
    }

    // =========================================================================
    // PRUEBAS: METODO findById()
    // =========================================================================

    /**
     * BUSCAR POR ID EXISTENTE: Valida que al proveer una clave primaria que sí se encuentra en el sistema,
     * el servicio extraiga la ficha médica y asigne sus valores correspondientes al DTO.
     */
    @Test
    void givenExistingId_whenFindById_thenReturnDto() {
        // GIVEN
        when(repository.findById(1L)).thenReturn(Optional.of(recordEntity));

        // WHEN
        MedicalRecordResponseDto result = service.findById(1L);

        // THEN
        assertNotNull(result);
        assertEquals("Asma", result.getDisease());
    }

    /**
     * BUSCAR ID INEXISTENTE: Comprueba que si se intenta buscar un ID que no existe, el repositorio devuelva
     * un Optional vacío y el servicio responda regresando null de manera limpia.
     */
    @Test
    void givenNonExistingId_whenFindById_thenReturnNull() {
        // GIVEN
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // WHEN
        MedicalRecordResponseDto result = service.findById(99L);

        // THEN
        assertNull(result);
    }

    // =========================================================================
    // PRUEBAS: METODO findByUserId() (Feign Client - Crítico)
    // =========================================================================

    /**
     * FILTRAR POR USUARIO EXISTENTE: Caso feliz. Verifica que si el cliente Feign encuentra al usuario,
     * se proceda de forma limpia a extraer todas las fichas médicas asociadas desde la BD local.
     */
    @Test
    void givenExistingUserInFeign_whenFindByUserId_thenReturnList() throws Exception {
        // GIVEN
        when(userClient.findById(10L)).thenReturn(mockUserResponse);
        when(repository.findByUserId(10L)).thenReturn(List.of(recordEntity));

        // WHEN
        List<MedicalRecordResponseDto> result = service.findByUserId(10L);

        // THEN
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(userClient, times(1)).findById(10L);
    }

    /**
     * FILTRAR POR USUARIO INEXISTENTE: Integridad de datos. Si Feign responde que el ID de usuario no existe
     * en el sistema principal (null), el servicio frena y nunca consulta la BD local de fichas médicas.
     */
    @Test
    void givenNonExistingUserInFeign_whenFindByUserId_thenReturnNull() throws Exception {
        // GIVEN
        when(userClient.findById(99L)).thenReturn(null);

        // WHEN
        List<MedicalRecordResponseDto> result = service.findByUserId(99L);

        // THEN
        assertNull(result);
        verify(repository, never()).findByUserId(anyLong());
    }

    /**
     * ERROR EN CLIENTE DE USUARIOS: Tolerancia a fallos. Si hay caídas de comunicación o un timeout con
     * el servicio externo, el sistema responde arrojando la excepción controlada.
     */
    @Test
    void givenFeignException_whenFindByUserId_throwException() {
        // GIVEN: Simulamos una caída de red o error de timeout en Feign Client
        when(userClient.findById(10L)).thenThrow(new RuntimeException("Timeout Connection"));

        // WHEN & THEN: Cambiado RuntimeException.class por Exception.class para coincidir con la firma del método
        Exception exception = assertThrows(Exception.class, () -> {
            service.findByUserId(10L);
        });

        assertTrue(exception.getMessage().contains("Timeout Connection"));
    }

    // =========================================================================
    // PRUEBAS: METODOS create() Y update()
    // =========================================================================

    /**
     * CREAR FICHA MÉDICA: Evalúa la inserción de una nueva ficha. Verifica que el objeto de entrada se envíe
     * a guardar y retorne el objeto resultante con su clave primaria correspondiente.
     */
    @Test
    void givenValidRequest_whenCreate_thenReturnCreatedDto() {
        // GIVEN
        when(repository.save(any(MedicalRecord.class))).thenReturn(recordEntity);

        // WHEN
        MedicalRecordResponseDto result = service.create(requestDto);

        // THEN
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    /**
     * ACTUALIZAR EXITOSO: Comprueba que si el ID de la ficha existe previamente en el sistema,
     * el servicio permita sobrescribir sus propiedades invocando la persistencia del repositorio.
     */
    @Test
    void givenExistingId_whenUpdate_thenReturnUpdatedDto() {
        // GIVEN
        when(repository.existsById(1L)).thenReturn(true);
        when(repository.save(any(MedicalRecord.class))).thenReturn(recordEntity);

        // WHEN
        MedicalRecordResponseDto result = service.update(1L, requestDto);

        // THEN
        assertNotNull(result);
        verify(repository, times(1)).save(any(MedicalRecord.class));
    }

    /**
     * ACTUALIZAR ID INEXISTENTE: Seguridad operativa. Si se intenta alterar una ficha apuntando a un ID inválido,
     * el servicio cancela el flujo devolviendo null y bloqueando accesos de escritura a la BD.
     */
    @Test
    void givenNonExistingId_whenUpdate_thenReturnNull() {
        // GIVEN
        when(repository.existsById(99L)).thenReturn(false);

        // WHEN
        MedicalRecordResponseDto result = service.update(99L, requestDto);

        // THEN
        assertNull(result);
        verify(repository, never()).save(any(MedicalRecord.class));
    }

    // =========================================================================
    // PRUEBAS: METODO deleteById()
    // =========================================================================

    /**
     * ELIMINAR EXITOSO: Verifica que si la clave primaria existe localmente, se proceda a ejecutar
     * la remoción del registro físico en el repositorio devolviendo la confirmación true.
     */
    @Test
    void givenExistingId_whenDeleteById_thenReturnTrue() {
        // GIVEN
        when(repository.existsById(1L)).thenReturn(true);
        doNothing().when(repository).deleteById(1L);

        // WHEN
        boolean result = service.deleteById(1L);

        // THEN
        assertTrue(result);
        verify(repository, times(1)).deleteById(1L);
    }

    /**
     * ELIMINAR ID INEXISTENTE: Comprueba que si se intenta borrar un ID corrupto o que ya no existe,
     * el servicio retorna un valor false controlado en lugar de generar excepciones inesperadas.
     */
    @Test
    void givenNonExistingId_whenDeleteById_thenReturnFalse() {
        // GIVEN
        when(repository.existsById(99L)).thenReturn(false);

        // WHEN
        boolean result = service.deleteById(99L);

        // THEN
        assertFalse(result);
        verify(repository, never()).deleteById(anyLong());
    }
}
