package cl.duoc.ms_auth.service.impl;

import cl.duoc.ms_auth.dto.*;
import cl.duoc.ms_auth.exception.UsernameAlreadyExistsException;
import cl.duoc.ms_auth.model.Credencial;
import cl.duoc.ms_auth.reporsitory.CredencialRepository;
import cl.duoc.ms_auth.security.JwtUtil;
import cl.duoc.ms_auth.service.api.UsuarioClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// Habilita el uso de Mockito en JUnit 5 para simular objetos sin levantar Spring
@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private CredencialRepository repository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UsuarioClient usuarioClient;

    @InjectMocks
    private AuthServiceImpl service;

    private Credencial credencialEntity;
    private AuthRequestDto loginRequest;
    private CredencialRequestDto registerRequest;

    @BeforeEach
    void setUp() {
        credencialEntity = new Credencial();
        credencialEntity.setIdCredencial(1L);
        credencialEntity.setUsername("felipe.perez");
        credencialEntity.setPassword("hash123");
        credencialEntity.setIdUser(10L);
        credencialEntity.setActive(true);

        loginRequest = new AuthRequestDto();
        loginRequest.setUsername("felipe.perez");
        loginRequest.setPassword("password123");

        registerRequest = new CredencialRequestDto();
        registerRequest.setUsername("felipe.perez");
        registerRequest.setPassword("password123");
        registerRequest.setIdUser(10L);
    }

    // =========================================================================
    // PRUEBAS: METODO login() (Camino Feliz y Errores)
    // =========================================================================

    /**
     * LOGIN EXITOSO: Evalúa que al ingresar credenciales correctas, el sistema valide
     * la clave hash, genere un token JWT con el rol correspondiente y devuelva un mensaje de éxito.
     */
    @Test
    void givenValidCredentials_whenLogin_thenReturnSuccessDto() {
        // GIVEN
        when(repository.findByUsername("felipe.perez")).thenReturn(Optional.of(credencialEntity));
        when(passwordEncoder.matches("password123", "hash123")).thenReturn(true);
        when(jwtUtil.generateToken("felipe.perez", 10L, "Cliente")).thenReturn("jwt-token");

        // WHEN
        AuthResponseDto response = service.login(loginRequest);

        // THEN
        assertNotNull(response);
        assertEquals("Login exitoso", response.getMensaje());
        assertEquals("jwt-token", response.getToken());
        assertEquals("Cliente", response.getRol());
    }

    /**
     * LOGIN USUARIO INEXISTENTE: Si el nombre de usuario no existe en la base de datos de credenciales,
     * el servicio debe interceptarlo retornando un mensaje controlado de credenciales inválidas sin token.
     */
    @Test
    void givenNonExistingUser_whenLogin_thenReturnInvalidCredentials() {
        // GIVEN
        when(repository.findByUsername("felipe.perez")).thenReturn(Optional.empty());

        // WHEN
        AuthResponseDto response = service.login(loginRequest);

        // THEN
        assertNotNull(response);
        assertEquals("Credenciales inválidas", response.getMensaje());
        assertNull(response.getToken());
    }

    /**
     * LOGIN CON CLAVE ERRÓNEA: Si el usuario existe pero la contraseña no coincide con el hash guardado,
     * el sistema frena la sesión devolviendo credenciales inválidas por seguridad.
     */
    @Test
    void givenWrongPassword_whenLogin_thenReturnInvalidCredentials() {
        // GIVEN
        when(repository.findByUsername("felipe.perez")).thenReturn(Optional.of(credencialEntity));
        when(passwordEncoder.matches("password123", "hash123")).thenReturn(false);

        // WHEN
        AuthResponseDto response = service.login(loginRequest);

        // THEN
        assertNotNull(response);
        assertEquals("Credenciales inválidas", response.getMensaje());
        assertNull(response.getToken());
    }

    // =========================================================================
    // PRUEBAS: METODO register() (Camino Feliz y Excepciones)
    // =========================================================================

    /**
     * REGISTRO EXITOSO: Comprueba que al enviar una solicitud de credenciales válida, se codifique la contraseña,
     * se guarde en el repositorio local y devuelva los datos del nuevo usuario.
     */
    @Test
    void givenValidRequest_whenRegister_thenReturnCreatedDto() {
        // GIVEN
        when(repository.findByUsername("felipe.perez")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("hash123");
        when(repository.save(any(Credencial.class))).thenReturn(credencialEntity);

        // WHEN
        CredencialResponseDto response = service.register(registerRequest);

        // THEN
        assertNotNull(response);
        assertEquals("felipe.perez", response.getUsername());
        verify(repository, times(1)).save(any(Credencial.class));
    }

    /**
     * REGISTRO CON USUARIO INEXISTENTE EN MICROSERVICIO: Integridad relacional. Si el microservicio de usuarios está caído
     * o no registra esa ID, se debe disparar un UsernameNotFoundException interrumpiendo el registro.
     */
    @Test
    void givenUserNotExistsInFeign_whenRegister_throwUsernameNotFoundException() {
        // GIVEN
        when(usuarioClient.findById(10L)).thenThrow(new RuntimeException("User server down"));

        // WHEN & THEN
        assertThrows(UsernameNotFoundException.class, () -> service.register(registerRequest));
        verify(repository, never()).save(any(Credencial.class));
    }

    /**
     * REGISTRO NOMBRE DUPLICADO: Valida la restricción única del sistema. Si el username ya está ocupado por otra persona,
     * el servicio frena la transacción lanzando un UsernameAlreadyExistsException.
     */
    @Test
    void givenDuplicateUsername_whenRegister_throwUsernameAlreadyExistsException() {
        // GIVEN
        when(repository.findByUsername("felipe.perez")).thenReturn(Optional.of(credencialEntity));

        // WHEN & THEN
        assertThrows(UsernameAlreadyExistsException.class, () -> service.register(registerRequest));
        verify(repository, never()).save(any(Credencial.class));
    }

    // =========================================================================
    // PRUEBAS: METODOS findAll() Y findById()
    // =========================================================================

    /**
     * BUSCAR TODOS: Verifica que el servicio acceda al repositorio, recupere la colección
     * completa de credenciales y la exponga en una lista mapeada a DTOs.
     */
    @Test
    void givenExistingRecords_whenFindAll_thenReturnList() {
        // GIVEN
        when(repository.findAll()).thenReturn(List.of(credencialEntity));

        // WHEN
        List<CredencialResponseDto> result = service.findAll();

        // THEN
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    /**
     * BUSCAR POR ID EXISTENTE: Valida que al proveer una clave primaria que sí se encuentra en el sistema,
     * el servicio extraiga el DTO y verifique que la cuenta se encuentra con estado activo.
     */
    @Test
    void givenExistingId_whenFindById_thenReturnDto() {
        // GIVEN
        when(repository.findById(1L)).thenReturn(Optional.of(credencialEntity));

        // WHEN
        CredencialResponseDto result = service.findById(1L);

        // THEN
        assertNotNull(result);
        assertTrue(result.getActivo());
    }

    // =========================================================================
    // PRUEBAS: METODO delete()
    // =========================================================================

    /**
     * ELIMINAR EXITOSO: Comprueba que si la clave primaria existe localmente, se proceda a ejecutar
     * la remoción física del registro en el repositorio devolviendo true.
     */
    @Test
    void givenExistingId_whenDelete_thenReturnTrue() {
        // GIVEN
        when(repository.existsById(1L)).thenReturn(true);
        doNothing().when(repository).deleteById(1L);

        // WHEN
        boolean result = service.delete(1L);

        // THEN
        assertTrue(result);
        verify(repository, times(1)).deleteById(1L);
    }
}
