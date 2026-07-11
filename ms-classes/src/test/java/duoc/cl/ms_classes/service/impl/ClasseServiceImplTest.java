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

// Habilita el entorno de pruebas unitarias puras con Mockito en JUnit 5 sin levantar infraestructura pesada
@ExtendWith(MockitoExtension.class)
class ClasseServiceImplTest {

    // Simula el repositorio local de la tabla de clases
    @Mock
    private ClasseRepository repository;

    // Inyecta el repositorio simulado de forma automática dentro del servicio real de clases
    @InjectMocks
    private ClasseServiceImpl service;

    private Classe classeEntity;
    private ClasseRequestDto requestDto;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    // Bloque preparatorio ejecutado automáticamente por JUnit antes de iniciar cada test individual
    @BeforeEach
    void setUp() {
        startDate = LocalDateTime.of(2026, 6, 22, 19, 0);
        endDate = LocalDateTime.of(2026, 6, 22, 20, 30);
        TypeClasses typeClasse = new TypeClasses(1L, "Crossfit");

        classeEntity = new Classe(1L, startDate, endDate, 20, 15, typeClasse);
        requestDto = new ClasseRequestDto(1L, startDate, endDate, 20, 15, typeClasse);
    }

    // =========================================================================
    // PRUEBAS: METODO findAll()
    // =========================================================================

    /**
     * BUSCAR TODOS: Verifica que el servicio acceda al repositorio, recupere el listado
     * completo de las clases programadas y las exponga correctamente mapeadas en DTOs.
     */
    @Test
    void givenExistingClasses_whenFindAll_thenReturnList() {
        // GIVEN: El repositorio local contiene un registro de clase guardada
        when(repository.findAll()).thenReturn(List.of(classeEntity));

        // WHEN: Invocamos el método del servicio para listar el historial
        List<ClasseResponseDto> result = service.findAll();

        // THEN: Confirmamos que la lista contenga los datos mapeados correctamente
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    // =========================================================================
    // PRUEBAS: METODO findById()
    // =========================================================================

    /**
     * BUSCAR POR ID EXISTENTE: Valida que al proveer una clave primaria que sí se encuentra en el sistema,
     * el servicio extraiga la clase, asigne sus valores al DTO y verifique los cupos disponibles.
     */
    @Test
    void givenExistingId_whenFindById_thenReturnResponseDto() {
        // GIVEN: El ID provisto corresponde a una clase existente en el sistema
        when(repository.findById(1L)).thenReturn(Optional.of(classeEntity));

        // WHEN: Solicitamos la clase por su ID único
        ClasseResponseDto result = service.findById(1L);

        // THEN: Validamos la exactitud de los cupos disponibles retornados en el DTO
        assertNotNull(result);
        assertEquals(15, result.getSpotsAvailable());
    }

    /**
     * BUSCAR ID VACÍO: Comprueba que si se intenta buscar un ID que no existe, el repositorio devuelva
     * un Optional vacío y el servicio responda regresando null de manera limpia.
     */
    @Test
    void givenNonExistingId_whenFindById_thenReturnNull() {
        // GIVEN: El ID consultado no figura en los registros de la base de datos
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // WHEN: Ejecutamos la búsqueda en el servicio de clases
        ClasseResponseDto result = service.findById(99L);

        // THEN: Verificamos que el retorno sea nulo
        assertNull(result);
    }

    // =========================================================================
    // PRUEBAS: METODO create()
    // =========================================================================

    /**
     * CREAR CLASE: Evalúa la inserción de una nueva clase. Verifica que el objeto se envíe
     * a guardar y retorne el objeto resultante con su clave primaria correspondiente.
     */
    @Test
    void givenValidRequest_whenCreate_thenReturnCreatedResponseDto() {
        // GIVEN: Configuramos al repositorio para que reciba cualquier entidad Classe y simule guardarla
        when(repository.save(any(Classe.class))).thenReturn(classeEntity);

        // WHEN: Invocamos la creación enviando el DTO de entrada
        ClasseResponseDto result = service.create(requestDto);

        // THEN: Comprobamos que el ID autogenerado se asigne de forma exitosa
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    // =========================================================================
    // PRUEBAS: METODO update()
    // =========================================================================

    /**
     * ACTUALIZAR EXITOSO: Comprueba que si el ID de la clase existe previamente en el sistema,
     * el servicio permita sobrescribir sus propiedades invocando la persistencia del repositorio.
     */
    @Test
    void givenExistingIdAndValidRequest_whenUpdate_thenReturnUpdatedResponseDto() {
        // GIVEN: La clase existe y el repositorio simula guardar las modificaciones con éxito
        when(repository.existsById(1L)).thenReturn(true);
        when(repository.save(any(Classe.class))).thenReturn(classeEntity);

        // WHEN: Mandamos a actualizar la información del ID 1
        ClasseResponseDto result = service.update(1L, requestDto);

        // THEN: Confirmamos la actualización verificando el llamado único a save()
        assertNotNull(result);
        verify(repository, times(1)).save(any(Classe.class));
    }

    /**
     * ACTUALIZAR ID INEXISTENTE: Seguridad operativa. Si se intenta alterar una clase apuntando a un ID corrupto,
     * el servicio cancela el flujo devolviendo null y bloqueando accesos de escritura a la BD.
     */
    @Test
    void givenNonExistingId_whenUpdate_thenReturnNull() {
        // GIVEN: El repositorio indica que el ID de clase consultado no se encuentra en el sistema
        when(repository.existsById(99L)).thenReturn(false);

        // WHEN: Intentamos modificar los datos apuntando al ID inválido
        ClasseResponseDto result = service.update(99L, requestDto);

        // THEN: Verificamos que el retorno sea nulo y que jamás (never) se intentara reescribir la entidad
        assertNull(result);
        verify(repository, never()).save(any(Classe.class));
    }

    // =========================================================================
    // PRUEBAS: METODO deleteById()
    // =========================================================================

    /**
     * ELIMINAR EXITOSO: Comprueba que si la clave primaria existe localmente, se proceda a ejecutar
     * la remoción del registro físico de la clase regresando una confirmación booleana verdadera.
     */
    @Test
    void givenExistingId_whenDeleteById_thenReturnTrue() {
        // GIVEN: Se valida la presencia del ID y se estipula que el método void no haga nada
        when(repository.existsById(1L)).thenReturn(true);
        doNothing().when(repository).deleteById(1L);

        // WHEN: Ejecutamos el borrado de la clase
        boolean result = service.deleteById(1L);

        // THEN: Aseguramos que responda verdadero
        assertTrue(result);
    }

    /**
     * ELIMINAR ID INEXISTENTE: Evita el desperdicio de procesamiento. Si la clase no se localiza,
     * se descarta invocar el método de eliminación física del repositorio y responde con bandera false.
     */
    @Test
    void givenNonExistingId_whenDeleteById_thenReturnFalse() {
        // GIVEN: El sistema notifica que el ID 99 no existe en los registros activos
        when(repository.existsById(99L)).thenReturn(false);

        // WHEN: Solicitamos borrar el ID inexistente
        boolean result = service.deleteById(99L);

        // THEN: Confirmamos que devuelva false y se proteja la sentencia de borrado evitando llamadas nulas
        assertFalse(result);
        verify(repository, never()).deleteById(99L);
    }
}
