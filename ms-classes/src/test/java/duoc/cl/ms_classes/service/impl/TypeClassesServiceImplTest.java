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

// Habilita el entorno de pruebas unitarias puras con Mockito en JUnit 5 sin levantar infraestructura pesada
@ExtendWith(MockitoExtension.class)
class TypeClassesServiceImplTest {

    // Simula el repositorio local de la tabla de tipos de clases
    @Mock
    private TypeClassesRepository repository;

    // Inyecta el repositorio simulado de forma automática dentro del servicio real
    @InjectMocks
    private TypeClassesServiceImpl service;

    private TypeClasses typeEntity;
    private TypeClassesRequestDto requestDto;

    // Bloque preparatorio ejecutado automáticamente por JUnit antes de iniciar cada test de forma individual
    @BeforeEach
    void setUp() {
        typeEntity = new TypeClasses(1L, "Natacion");
        requestDto = new TypeClassesRequestDto(1L, "Natacion");
    }

    // =========================================================================
    // PRUEBAS: METODO findAll()
    // =========================================================================

    /**
     * BUSCAR TODOS: Verifica que el servicio acceda al repositorio, recupere la colección
     * completa de disciplinas/tipos de clases y las exponga mapeadas en DTOs.
     */
    @Test
    void givenExistingRecords_whenFindAll_thenReturnList() {
        // GIVEN: El repositorio local contiene un registro de tipo de clase guardado
        when(repository.findAll()).thenReturn(List.of(typeEntity));

        // WHEN: Invocamos el método del servicio para listar el historial
        List<TypeClassesResponseDto> result = service.findAll();

        // THEN: Confirmamos el tamaño de la lista resultante y la llamada única al repositorio
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(repository, times(1)).findAll();
    }

    // =========================================================================
    // PRUEBAS: METODO findById()
    // =========================================================================

    /**
     * BUSCAR POR ID EXISTENTE: Valida que al proveer una clave primaria que sí se encuentra en el sistema,
     * el servicio extraiga la entidad del Optional y asigne el nombre correspondiente al DTO.
     */
    @Test
    void givenExistingId_whenFindById_thenReturnResponseDto() {
        // GIVEN: El ID provisto corresponde a una disciplina existente en el sistema
        when(repository.findById(1L)).thenReturn(Optional.of(typeEntity));

        // WHEN: Solicitamos la disciplina por su ID único
        TypeClassesResponseDto result = service.findById(1L);

        // THEN: Validamos la exactitud del nombre retornado en el DTO resultante
        assertNotNull(result);
        assertEquals("Natacion", result.getName());
    }

    /**
     * BUSCAR ID VACÍO: Comprueba que si se intenta buscar un ID que no existe, el repositorio devuelva
     * un Optional vacío y el servicio responda regresando null de manera limpia.
     */
    @Test
    void givenNonExistingId_whenFindById_thenReturnNull() {
        // GIVEN: El ID consultado no figura en los registros de la base de datos
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // WHEN: Ejecutamos la búsqueda en el servicio
        TypeClassesResponseDto result = service.findById(99L);

        // THEN: Verificamos que el retorno sea nulo
        assertNull(result);
    }

    // =========================================================================
    // PRUEBAS: METODO create()
    // =========================================================================

    /**
     * CREAR DISCIPLINA: Evalúa la inserción de un nuevo tipo de clase. Verifica que el objeto se envíe
     * a guardar y retorne el objeto resultante con su clave primaria correspondiente.
     */
    @Test
    void givenValidRequest_whenCreate_thenReturnCreatedResponseDto() {
        // GIVEN: Configuramos al repositorio para que reciba cualquier entidad TypeClasses y simule guardarla
        when(repository.save(any(TypeClasses.class))).thenReturn(typeEntity);

        // WHEN: Invocamos la creación enviando el DTO de entrada
        TypeClassesResponseDto result = service.create(requestDto);

        // THEN: Comprobamos que el ID autogenerado se asigne de forma exitosa
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    // =========================================================================
    // PRUEBAS: METODO update()
    // =========================================================================

    /**
     * ACTUALIZAR EXITOSO: Comprueba que si el ID de la disciplina existe previamente en el sistema,
     * el servicio permita sobrescribir sus propiedades invocando la persistencia del repositorio.
     */
    @Test
    void givenExistingIdAndValidRequest_whenUpdate_thenReturnUpdatedResponseDto() {
        // GIVEN: La disciplina existe y el repositorio simula guardar las modificaciones con éxito
        when(repository.existsById(1L)).thenReturn(true);
        when(repository.save(any(TypeClasses.class))).thenReturn(typeEntity);

        // WHEN: Mandamos a actualizar la información del ID 1
        TypeClassesResponseDto result = service.update(1L, requestDto);

        // THEN: Confirmamos la actualización verificando el llamado único a save()
        assertNotNull(result);
        verify(repository, times(1)).save(any(TypeClasses.class));
    }

    /**
     * ACTUALIZAR ID INEXISTENTE: Seguridad operativa. Si se intenta alterar una disciplina apuntando a un ID inválido,
     * el servicio cancela el flujo devolviendo null y bloqueando accesos de escritura a la BD.
     */
    @Test
    void givenNonExistingId_whenUpdate_thenReturnNull() {
        // GIVEN: El repositorio indica que el ID consultado no se encuentra en el sistema
        when(repository.existsById(99L)).thenReturn(false);

        // WHEN: Intentamos modificar los datos apuntando al ID inválido
        TypeClassesResponseDto result = service.update(99L, requestDto);

        // THEN: Verificamos que el retorno sea nulo y que jamás (never) se intentara reescribir la entidad
        assertNull(result);
        verify(repository, never()).save(any(TypeClasses.class));
    }

    // =========================================================================
    // PRUEBAS: METODO deleteById()
    // =========================================================================

    /**
     * ELIMINAR EXITOSO: Comprueba que si la clave primaria existe localmente, se proceda a ejecutar
     * la remoción del registro físico en el repositorio devolviendo la confirmación booleana verdadera.
     */
    @Test
    void givenExistingId_whenDeleteById_thenReturnTrue() {
        // GIVEN: Se valida la presencia del ID y se estipula que el método void no haga nada
        when(repository.existsById(1L)).thenReturn(true);
        doNothing().when(repository).deleteById(1L);

        // WHEN: Ejecutamos el borrado de la disciplina
        boolean result = service.deleteById(1L);

        // THEN: Aseguramos que responda verdadero
        assertTrue(result);
    }

    /**
     * ELIMINAR ID INEXISTENTE: Evita el desperdicio de procesamiento. Si la disciplina no se localiza,
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
