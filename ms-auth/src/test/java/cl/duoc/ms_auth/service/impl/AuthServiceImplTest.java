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

    // ==========================================
    // PRUEBAS: login() (Camino Feliz y Errores)
    // ==========================================
    @Test
    void givenValidCredentials_whenLogin_thenReturnSuccessDto() {
        when(repository.findByUsername("felipe.perez")).thenReturn(Optional.of(credencialEntity));
        when(passwordEncoder.matches("password123", "hash123")).thenReturn(true);
        when(jwtUtil.generateToken("felipe.perez", 10L, "Cliente")).thenReturn("jwt-token");

        AuthResponseDto response = service.login(loginRequest);

        assertNotNull(response);
        assertEquals("Login exitoso", response.getMensaje());
        assertEquals("jwt-token", response.getToken());
        assertEquals("Cliente", response.getRol());
    }

    @Test
    void givenNonExistingUser_whenLogin_thenReturnInvalidCredentials() {
        when(repository.findByUsername("felipe.perez")).thenReturn(Optional.empty());

        AuthResponseDto response = service.login(loginRequest);

        assertNotNull(response);
        assertEquals("Credenciales inválidas", response.getMensaje());
        assertNull(response.getToken());
    }

    @Test
    void givenWrongPassword_whenLogin_thenReturnInvalidCredentials() {
        when(repository.findByUsername("felipe.perez")).thenReturn(Optional.of(credencialEntity));
        when(passwordEncoder.matches("password123", "hash123")).thenReturn(false);

        AuthResponseDto response = service.login(loginRequest);

        assertNotNull(response);
        assertEquals("Credenciales inválidas", response.getMensaje());
        assertNull(response.getToken());
    }

    // ==========================================
    // PRUEBAS: register() (Camino Feliz y Excepciones)
    // ==========================================
    @Test
    void givenValidRequest_whenRegister_thenReturnCreatedDto() {
        when(repository.findByUsername("felipe.perez")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("hash123");
        when(repository.save(any(Credencial.class))).thenReturn(credencialEntity);

        CredencialResponseDto response = service.register(registerRequest);

        assertNotNull(response);
        assertEquals("felipe.perez", response.getUsername());
        verify(repository, times(1)).save(any(Credencial.class));
    }

    @Test
    void givenUserNotExistsInFeign_whenRegister_throwUsernameNotFoundException() {
        when(usuarioClient.findById(10L)).thenThrow(new RuntimeException("User server down"));

        assertThrows(UsernameNotFoundException.class, () -> service.register(registerRequest));
        verify(repository, never()).save(any(Credencial.class));
    }

    @Test
    void givenDuplicateUsername_whenRegister_throwUsernameAlreadyExistsException() {
        when(repository.findByUsername("felipe.perez")).thenReturn(Optional.of(credencialEntity));

        assertThrows(UsernameAlreadyExistsException.class, () -> service.register(registerRequest));
        verify(repository, never()).save(any(Credencial.class));
    }

    // ==========================================
    // PRUEBAS: findAll() y findById()
    // ==========================================
    @Test
    void givenExistingRecords_whenFindAll_thenReturnList() {
        when(repository.findAll()).thenReturn(List.of(credencialEntity));
        List<CredencialResponseDto> result = service.findAll();
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void givenExistingId_whenFindById_thenReturnDto() {
        when(repository.findById(1L)).thenReturn(Optional.of(credencialEntity));
        CredencialResponseDto result = service.findById(1L);
        assertNotNull(result);
        assertTrue(result.getActivo());
    }

    // ==========================================
    // PRUEBAS: delete()
    // ==========================================
    @Test
    void givenExistingId_whenDelete_thenReturnTrue() {
        when(repository.existsById(1L)).thenReturn(true);
        doNothing().when(repository).deleteById(1L);

        boolean result = service.delete(1L);

        assertTrue(result);
        verify(repository, times(1)).deleteById(1L);
    }
}
