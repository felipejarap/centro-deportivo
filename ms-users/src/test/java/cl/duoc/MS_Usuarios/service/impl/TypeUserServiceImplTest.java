package cl.duoc.MS_Usuarios.service.impl;

import cl.duoc.MS_Usuarios.dto.TypeUserRequestDto;
import cl.duoc.MS_Usuarios.dto.TypeUserResponseDto;
import cl.duoc.MS_Usuarios.model.TypeUser;
import cl.duoc.MS_Usuarios.model.User;
import cl.duoc.MS_Usuarios.repository.TypeUserRepository;
import cl.duoc.MS_Usuarios.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// Inicializa Mockito para JUnit 5 permitiendo simular repositorios sin levantar la base de datos real o H2
@ExtendWith(MockitoExtension.class)
class TypeUserServiceImplTest {

    @Mock
    private TypeUserRepository repository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TypeUserServiceImpl service;

    private TypeUser typeUserEntity;
    private TypeUserRequestDto requestDto;

    @BeforeEach
    void setUp() {
        typeUserEntity = new TypeUser();
        typeUserEntity.setId(1L);
        typeUserEntity.setName("Admin");

        requestDto = new TypeUserRequestDto();
        requestDto.setName("Admin");
    }

    // =========================================================================
    // PRUEBAS: METODOS findAll() Y findById()
    // =========================================================================

    /**
     * BUSCAR TODOS: Evalúa que al invocar findAll(), el servicio consulte al repositorio
     * y devuelva una lista mapeada a objetos ResponseDto con el tamaño correcto.
     */
    @Test
    void givenExistingRecords_whenFindAll_thenReturnList() {
        // GIVEN
        when(repository.findAll()).thenReturn(List.of(typeUserEntity));

        // WHEN
        List<TypeUserResponseDto> result = service.findAll();

        // THEN
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    /**
     * BUSCAR POR ID EXISTENTE: Verifica que cuando se proporciona un ID válido que sí existe,
     * el servicio extraiga la entidad del Optional, la procese y devuelva los datos correctos.
     */
    @Test
    void givenExistingId_whenFindById_thenReturnDto() {
        // GIVEN
        when(repository.findById(1L)).thenReturn(Optional.of(typeUserEntity));

        // WHEN
        TypeUserResponseDto result = service.findById(1L);

        // THEN
        assertNotNull(result);
        assertEquals("Admin", result.getName());
    }

    // =========================================================================
    // PRUEBAS: METODO create() (Camino Feliz y Conflicto)
    // =========================================================================

    /**
     * CREAR EXITOSO: Evalúa el flujo feliz donde el nombre no está duplicado en el sistema,
     * permitiendo que Hibernate guíe el registro nuevo de forma normal.
     */
    @Test
    void givenNewName_whenCreate_thenReturnCreatedDto() {
        // GIVEN
        when(repository.findByNameIgnoreCase("Admin")).thenReturn(Optional.empty());
        when(repository.save(any(TypeUser.class))).thenReturn(typeUserEntity);

        // WHEN
        TypeUserResponseDto result = service.create(requestDto);

        // THEN
        assertNotNull(result);
        assertEquals("Admin", result.getName());
    }

    /**
     * CREAR DUPLICADO: Valida la regla de negocio que prohíbe nombres repetidos. Si ya existe,
     * debe arrojar un error HTTP 409 CONFLICT y prohibir explícitamente el guardado.
     */
    @Test
    void givenDuplicateName_whenCreate_throwConflictException() {
        // GIVEN
        when(repository.findByNameIgnoreCase("Admin")).thenReturn(Optional.of(typeUserEntity));

        // WHEN & THEN
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            service.create(requestDto);
        });

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        assertTrue(exception.getReason().contains("Ya existe un tipo de usuario"));
        verify(repository, never()).save(any(TypeUser.class));
    }

    // =========================================================================
    // PRUEBAS: METODO update() (Camino Feliz, ID Vacío y Conflicto)
    // =========================================================================

    /**
     * ACTUALIZAR EXITOSO: Valida que si el ID existe y el nuevo nombre está disponible o pertenece
     * a la misma entidad, los cambios se guarden correctamente.
     */
    @Test
    void givenValidIdAndRequest_whenUpdate_thenReturnUpdatedDto() {
        // GIVEN
        when(repository.findById(1L)).thenReturn(Optional.of(typeUserEntity));
        when(repository.findByNameIgnoreCase("Admin")).thenReturn(Optional.empty());
        when(repository.save(any(TypeUser.class))).thenReturn(typeUserEntity);

        // WHEN
        TypeUserResponseDto result = service.update(1L, requestDto);

        // THEN
        assertNotNull(result);
        verify(repository, times(1)).save(any(TypeUser.class));
    }

    /**
     * ACTUALIZAR ID INEXISTENTE: Si intentamos modificar un registro apuntando a un ID inválido,
     * el servicio debe interceptarlo retornando null de inmediato sin interactuar más.
     */
    @Test
    void givenNonExistingId_whenUpdate_thenReturnNull() {
        // GIVEN
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // WHEN
        TypeUserResponseDto result = service.update(99L, requestDto);

        // THEN
        assertNull(result);
        verify(repository, never()).save(any(TypeUser.class));
    }

    /**
     * ACTUALIZAR CON NOMBRE DUPLICADO: Escenario crítico. Si intentas cambiar el nombre del ID 1
     * a uno que ya está ocupado por el ID 2, el sistema debe disparar una excepción HTTP 409.
     */
    @Test
    void givenDuplicateNameOtherId_whenUpdate_throwConflictException() {
        // GIVEN
        TypeUser secondaryEntity = new TypeUser();
        secondaryEntity.setId(2L);
        secondaryEntity.setName("Admin");

        when(repository.findById(1L)).thenReturn(Optional.of(typeUserEntity));
        when(repository.findByNameIgnoreCase("Admin")).thenReturn(Optional.of(secondaryEntity));

        // WHEN & THEN
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            service.update(1L, requestDto);
        });

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
    }

    // =========================================================================
    // PRUEBAS: METODO deleteById() (Integridad Referencial de Datos)
    // =========================================================================

    /**
     * ELIMINAR EXITOSO: Si el registro existe y ninguna otra tabla depende de él (cero usuarios amarrados),
     * se procede con la eliminación física en la base de datos retornando true.
     */
    @Test
    void givenExistingIdAndNoUsersAssigned_whenDeleteById_thenReturnTrue() {
        // GIVEN
        when(repository.existsById(1L)).thenReturn(true);
        when(userRepository.findByTypeUser_Id(1L)).thenReturn(Collections.emptyList());

        // WHEN
        boolean result = service.deleteById(1L);

        // THEN
        assertTrue(result);
        verify(repository, times(1)).deleteById(1L);
    }

    /**
     * ELIMINAR ID INEXISTENTE: Comprueba que si se intenta borrar algo que ya no existe,
     * el método responde false limpiamente sin provocar errores internos en cascada.
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

    /**
     * ELIMINAR CON LLAVE FORÁNEA (REGLA DE NEGOCIO): Evita la pérdida de integridad de datos.
     * Si intentas borrar un Tipo de Usuario que actualmente tienen asignado varios usuarios activos,
     * el servicio frena la operación lanzando una excepción HTTP 409.
     */
    @Test
    void givenIdWithAssignedUsers_whenDeleteById_throwConflictException() {
        // GIVEN
        when(repository.existsById(1L)).thenReturn(true);
        when(userRepository.findByTypeUser_Id(1L)).thenReturn(List.of(new User()));

        // WHEN & THEN
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            service.deleteById(1L);
        });

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        assertTrue(exception.getReason().contains("No se puede eliminar"));
        verify(repository, never()).deleteById(anyLong());
    }
}
