package cl.duoc.MS_Usuarios.service.impl;

import cl.duoc.MS_Usuarios.dto.UserRequestDto;
import cl.duoc.MS_Usuarios.dto.UserResponseDto;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// Inicializa Mockito para JUnit 5, permitiendo simular el comportamiento de los repositorios de datos
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository repository;

    @Mock
    private TypeUserRepository typeUserRepository;

    @InjectMocks
    private UserServiceImpl service;

    private User userEntity;
    private TypeUser typeUserEntity;
    private UserRequestDto requestDto;

    @BeforeEach
    void setUp() {
        typeUserEntity = new TypeUser();
        typeUserEntity.setId(1L);
        typeUserEntity.setName("Admin");

        userEntity = new User();
        userEntity.setIdUser(1L);
        userEntity.setUsername("Eduardo");
        userEntity.setPaternalSurname("Urquieta");
        userEntity.setMaternalSurname("Cruz");
        userEntity.setEmail("ed.urquieta@duocuc.cl");
        userEntity.setPhone("+56912345678");
        userEntity.setTypeUser(typeUserEntity);

        requestDto = new UserRequestDto();
        requestDto.setUsername("Eduardo");
        requestDto.setPaternalSurname("Urquieta");
        requestDto.setMaternalSurname("Cruz");
        requestDto.setEmail("ed.urquieta@duocuc.cl");
        requestDto.setPhone("+56912345678");
        requestDto.setTypeUserId(1L);
    }

    // =========================================================================
    // PRUEBAS: METODOS findAll() Y findById()
    // =========================================================================

    /**
     * BUSCAR TODOS: Verifica que al listar usuarios, se use la consulta optimizada con Join
     * (findAllWithTypeUser) y devuelva la lista correctamente mapeada a DTOs.
     */
    @Test
    void givenExistingUsers_whenFindAll_thenReturnList() {
        // GIVEN
        when(repository.findAllWithTypeUser()).thenReturn(List.of(userEntity));

        // WHEN
        List<UserResponseDto> result = service.findAll();

        // THEN
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Eduardo", result.get(0).getUsername());
    }

    /**
     * BUSCAR POR ID EXISTENTE: Valida que al buscar por una clave primaria válida, el servicio
     * procese el Optional retornado y entregue los datos esperados del usuario.
     */
    @Test
    void givenExistingId_whenFindById_thenReturnDto() {
        // GIVEN
        when(repository.findByIdWithTypeUser(1L)).thenReturn(Optional.of(userEntity));

        // WHEN
        UserResponseDto result = service.findById(1L);

        // THEN
        assertNotNull(result);
        assertEquals("Eduardo", result.getUsername());
    }

    // =========================================================================
    // PRUEBAS: METODO findByTypeUserId() (Filtro por Tipo de Usuario)
    // =========================================================================

    /**
     * FILTRAR POR TIPO EXISTENTE: Comprueba el flujo feliz donde el tipo de usuario sí existe,
     * obteniendo con éxito la lista de usuarios asociados a dicho rol.
     */
    @Test
    void givenExistingTypeUserId_whenFindByTypeUserId_thenReturnList() {
        // GIVEN
        when(typeUserRepository.existsById(1L)).thenReturn(true);
        when(repository.findByTypeUser_Id(1L)).thenReturn(List.of(userEntity));

        // WHEN
        List<UserResponseDto> result = service.findByTypeUserId(1L);

        // THEN
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    /**
     * FILTRAR POR TIPO INEXISTENTE: Si se busca por un ID de rol inexistente (ej: 99), el sistema
     * debe lanzar un HTTP 404 NOT FOUND de inmediato y bloquear la consulta de usuarios.
     */
    @Test
    void givenNonExistingTypeUserId_whenFindByTypeUserId_throwNotFoundException() {
        // GIVEN
        when(typeUserRepository.existsById(99L)).thenReturn(false);

        // WHEN & THEN
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            service.findByTypeUserId(99L);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(repository, never()).findByTypeUser_Id(anyLong());
    }

    // =========================================================================
    // PRUEBAS: METODO create()
    // =========================================================================

    /**
     * CREAR EXITOSO: Evalúa la inserción. Si el tipo de usuario de la petición es
     * válido, la entidad se mapea, se guarda en el repositorio y retorna el DTO con su ID.
     */
    @Test
    void givenValidRequest_whenCreate_thenReturnCreatedDto() {
        // GIVEN
        when(typeUserRepository.findById(1L)).thenReturn(Optional.of(typeUserEntity));
        when(repository.save(any(User.class))).thenReturn(userEntity);

        // WHEN
        UserResponseDto result = service.create(requestDto);

        // THEN
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(repository, times(1)).save(any(User.class));
    }

    /**
     * CREAR CON TIPO DE USUARIO INVÁLIDO: Regla de integridad. Si se intenta registrar un usuario
     * asignándole un rol inexistente, la aplicación frena el flujo con un HTTP 400 BAD REQUEST.
     */
    @Test
    void givenInvalidTypeUserId_whenCreate_throwBadRequestException() {
        // GIVEN
        requestDto.setTypeUserId(99L);
        when(typeUserRepository.findById(99L)).thenReturn(Optional.empty());

        // WHEN & THEN
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            service.create(requestDto);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        verify(repository, never()).save(any(User.class));
    }

    // =========================================================================
    // PRUEBAS: METODO update()
    // =========================================================================

    /**
     * ACTUALIZAR EXITOSO: Valida que si el ID de usuario existe previamente en el sistema y el nuevo ID de rol es válido,
     * el repositorio consolida y guarda los cambios correspondientes.
     */
    @Test
    void givenExistingIdAndValidRequest_whenUpdate_thenReturnUpdatedDto() {
        // GIVEN
        when(repository.existsById(1L)).thenReturn(true);
        when(typeUserRepository.findById(1L)).thenReturn(Optional.of(typeUserEntity));
        when(repository.save(any(User.class))).thenReturn(userEntity);

        // WHEN
        UserResponseDto result = service.update(1L, requestDto);

        // THEN
        assertNotNull(result);
        verify(repository, times(1)).save(any(User.class));
    }

    /**
     * ACTUALIZAR ID INEXISTENTE: Si se intenta modificar un usuario cuyo ID no existe en el sistema,
     * el método debe retornar un valor nulo limpiamente sin alterar ningún registro.
     */
    @Test
    void givenNonExistingUserId_whenUpdate_thenReturnNull() {
        // GIVEN
        when(repository.existsById(99L)).thenReturn(false);

        // WHEN
        UserResponseDto result = service.update(99L, requestDto);

        // THEN
        assertNull(result);
        verify(repository, never()).save(any(User.class));
    }

    // =========================================================================
    // PRUEBAS: METODO delete()
    // =========================================================================

    /**
     * ELIMINACIÓN EXITOSA: Comprueba que si el usuario existe, se ejecuta de forma física
     * el método de remoción en el repositorio y responde un valor de confirmación true.
     */
    @Test
    void givenExistingUserId_whenDelete_thenReturnTrue() {
        // GIVEN
        when(repository.existsById(1L)).thenReturn(true);
        doNothing().when(repository).deleteById(1L);

        // WHEN
        boolean result = service.delete(1L);

        // THEN
        assertTrue(result);
        verify(repository, times(1)).deleteById(1L);
    }

    /**
     * ELIMINACIÓN FALLIDA: Si se intenta borrar un ID de usuario que no existe, el servicio
     * lo detecta de inmediato, aborta la operación y retorna false para notificarlo.
     */
    @Test
    void givenNonExistingUserId_whenDelete_thenReturnFalse() {
        // GIVEN
        when(repository.existsById(99L)).thenReturn(false);

        // WHEN
        boolean result = service.delete(99L);

        // THEN
        assertFalse(result);
        verify(repository, never()).deleteById(anyLong());
    }
}
