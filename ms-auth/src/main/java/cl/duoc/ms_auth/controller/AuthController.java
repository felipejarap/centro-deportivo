package cl.duoc.ms_auth.controller;

import cl.duoc.ms_auth.dto.AuthRequestDto;
import cl.duoc.ms_auth.dto.AuthResponseDto;
import cl.duoc.ms_auth.dto.CredencialRequestDto;
import cl.duoc.ms_auth.dto.CredencialResponseDto;
import cl.duoc.ms_auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticación", description = "Endpoints para login, registro y administración de credenciales de acceso")
public class AuthController {

    private final AuthService service;
    // Login — obtener token
    @Operation(summary = "Iniciar sesión", description = "Valida las credenciales de un usuario y, si son correctas, retorna un token JWT de acceso.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Autenticación exitosa, token generado",
                content = @Content(schema = @Schema(implementation = AuthResponseDto.class))),
                 @ApiResponse(responseCode = "401", description = "Credenciales inválidas", content = @Content),
                 @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o formato incompatible", content = @Content)
   })
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(
            @Valid @RequestBody AuthRequestDto request) {
        AuthResponseDto response = service.login(request);
        if (response.getToken() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        return ResponseEntity.ok(response);
    }
    @Operation(summary = "Registrar credenciales", description = "Crea las credenciales de acceso (usuario y contraseña) asociadas a un usuario del centro deportivo.")
    @ApiResponses(value = {
                         @ApiResponse(responseCode = "201", description = "Credenciales registradas exitosamente",
                                 content = @Content(schema = @Schema(implementation = CredencialResponseDto.class))),
                         @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o nombre de usuario ya existente", content = @Content)
   })

    // Registrar credenciales para un usuario
    @PostMapping("/register")
    public ResponseEntity<CredencialResponseDto> register(
            @Valid @RequestBody CredencialRequestDto request) {
        CredencialResponseDto response = service.register(request);
        if (response == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Listar credenciales", description = "Recupera el listado completo de credenciales registradas en el sistema.")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Listado recuperado exitosamente") })

    @GetMapping("/credenciales")
    public ResponseEntity<List<CredencialResponseDto>> findAll() {

        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Buscar credencial por ID", description = "Recupera el detalle de una credencial específica mediante su identificador único.")
    @ApiResponses(value = {
                         @ApiResponse(responseCode = "200", description = "Credencial encontrada",
                                 content = @Content(schema = @Schema(implementation = CredencialResponseDto.class))),
                         @ApiResponse(responseCode = "404", description = "No se encontró ninguna credencial con el ID proporcionado", content = @Content)
    })

    @GetMapping("/credenciales/{id}")
    public ResponseEntity<CredencialResponseDto> findById(@PathVariable Long id) {
        CredencialResponseDto credencial = service.findById(id);
        if (credencial == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(credencial);
    }

    @Operation(summary = "Eliminar credencial", description = "Elimina permanentemente las credenciales de acceso de un usuario.")
    @ApiResponses(value = {
                         @ApiResponse(responseCode = "204", description = "Credencial eliminada exitosamente (Sin contenido)", content = @Content),
                         @ApiResponse(responseCode = "404", description = "No se encontró la credencial solicitada", content = @Content)
    })

    @DeleteMapping("/credenciales/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (service.delete(id)) return ResponseEntity.noContent().build();
        return ResponseEntity.notFound().build();
    }
}
