package cl.duoc.ms_auth.controller;

import cl.duoc.ms_auth.dto.AuthRequestDto;
import cl.duoc.ms_auth.dto.AuthResponseDto;
import cl.duoc.ms_auth.dto.CredencialRequestDto;
import cl.duoc.ms_auth.dto.CredencialResponseDto;
import cl.duoc.ms_auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;
    // Login — obtener token
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(
            @Valid @RequestBody AuthRequestDto request) {
        AuthResponseDto response = service.login(request);
        if (response.getToken() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        return ResponseEntity.ok(response);
    }

    // Registrar credenciales para un usuario
    @PostMapping("/register")
    public ResponseEntity<CredencialResponseDto> register(
            @Valid @RequestBody CredencialRequestDto request) {
        CredencialResponseDto response = service.register(request);
        if (response == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/credenciales")
    public ResponseEntity<List<CredencialResponseDto>> findAll() {

        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/credenciales/{id}")
    public ResponseEntity<CredencialResponseDto> findById(@PathVariable Long id) {
        CredencialResponseDto credencial = service.findById(id);
        if (credencial == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(credencial);
    }

    @DeleteMapping("/credenciales/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (service.delete(id)) return ResponseEntity.noContent().build();
        return ResponseEntity.notFound().build();
    }
}
