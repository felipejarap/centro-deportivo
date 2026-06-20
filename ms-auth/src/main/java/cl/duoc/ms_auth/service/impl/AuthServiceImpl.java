package cl.duoc.ms_auth.service.impl;

import cl.duoc.ms_auth.dto.AuthRequestDto;
import cl.duoc.ms_auth.dto.AuthResponseDto;
import cl.duoc.ms_auth.dto.CredencialRequestDto;
import cl.duoc.ms_auth.dto.CredencialResponseDto;
import cl.duoc.ms_auth.exception.UserNotFoundException;
import cl.duoc.ms_auth.exception.UsernameAlreadyExistsException;
import cl.duoc.ms_auth.model.Credencial;
import cl.duoc.ms_auth.reporsitory.CredencialRepository;
import cl.duoc.ms_auth.security.JwtUtil;
import cl.duoc.ms_auth.service.AuthService;
import cl.duoc.ms_auth.service.api.UsuarioClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final CredencialRepository repository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioClient usuarioClient;

    private CredencialResponseDto toDto(Credencial entity) {
        return new CredencialResponseDto(
                entity.getIdCredencial(),
                entity.getUsername(),
                entity.getIdUser(),
                entity.getActive()
        );
    }

    @Override
    public AuthResponseDto login(AuthRequestDto request) {
        log.info("Intento de login para username: {}", request.getUsername());

        Credencial credencial = repository.findByUsername(request.getUsername())
                .orElse(null);

        if (credencial == null || !credencial.getActive()) {
            log.warn("Username no encontrado o inactivo: {}", request.getUsername());
            return new AuthResponseDto(null, null, null, null, "Credenciales inválidas");
        }

        if (!passwordEncoder.matches(request.getPassword(), credencial.getPassword())) {
            log.warn("Password incorrecto para: {}", request.getUsername());
            return new AuthResponseDto(null, null, null, null, "Credenciales inválidas");
        }

        String rol = "Cliente";
        try {
            var usuario = usuarioClient.findById(credencial.getIdUser());
            if (usuario != null && usuario.getTypeUser() != null && usuario.getTypeUser().getName() != null) {
                rol = usuario.getTypeUser().getName();
            }
        } catch (Exception e) {
            log.warn("No se pudo obtener el tipo de usuario para id {}: {}", credencial.getIdUser(), e.getMessage());
        }

        String token = jwtUtil.generateToken(
                credencial.getUsername(),
                credencial.getIdUser(),
                rol
        );

        log.info("Login exitoso para: {} con rol: {}", request.getUsername(), rol);
        return new AuthResponseDto(token, credencial.getUsername(),
                credencial.getIdUser(), rol, "Login exitoso");
    }

    @Override
    public CredencialResponseDto register(CredencialRequestDto request) {
        log.info("Registrando credencial para idUsuario: {}", request.getIdUser());

        // Verificar que el usuario existe en ms-usuarios
        try {
            usuarioClient.findById(request.getIdUser());
        } catch (Exception e) {
            log.warn("Usuario {} no encontrado en ms-usuarios", request.getIdUser());
            throw new UsernameNotFoundException(
                    "El usuario con id" + request.getIdUser() + " no existe");
        }
        if (repository.findByUsername(request.getUsername()).isPresent()) {
            log.warn("Username ya registrado:{}",request.getUsername());
            throw new UsernameAlreadyExistsException(
                    "El username '" + request.getUsername() + "' ya está en uso");
        }

        Credencial credencial = new Credencial();
        credencial.setUsername(request.getUsername());
        credencial.setPassword(passwordEncoder.encode(request.getPassword())); // ← hashea el password
        credencial.setIdUser(request.getIdUser());
        credencial.setActive(true);

        return toDto(repository.save(credencial));
    }

    @Override
    public List<CredencialResponseDto> findAll() {
        return repository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public CredencialResponseDto findById(Long id) {
        return repository.findById(id).map(this::toDto).orElse(null);
    }

    @Override
    public boolean delete(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
