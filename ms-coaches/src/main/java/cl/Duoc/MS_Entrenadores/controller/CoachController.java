package cl.Duoc.MS_Entrenadores.controller;
import cl.Duoc.MS_Entrenadores.dto.CoachRequestDto;
import cl.Duoc.MS_Entrenadores.dto.CoachResponseDto;
import cl.Duoc.MS_Entrenadores.service.CoachService;
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
@RequestMapping("/api/v1/coaches")
@RequiredArgsConstructor
@Tag(name = "Gestión de Entrenadores", description = "Endpoints para la creación, actualización, consulta y desvinculación del personal de entrenamiento")
public class CoachController {

    private final CoachService service;

    @Operation(summary = "Obtener todos los entrenadores", description = "Recupera una lista completa con todos los profesores, instructores y entrenadores deportivos registrados en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de entrenadores recuperado exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<CoachResponseDto>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }
    @Operation(summary = "Buscar entrenador por ID", description = "Recupera la información pública, especialidades y certificaciones de un entrenador mediante su identificador único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entrenador encontrado con éxito",
                    content = @Content(schema = @Schema(implementation = CoachResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "No se encontró ningún entrenador con el ID proporcionado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<CoachResponseDto> findById(@PathVariable Long id) {
        CoachResponseDto coach = service.findById(id);
        if (coach == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(coach);
    }
    @Operation(summary = "Registrar un nuevo entrenador", description = "Crea un perfil de entrenador en el sistema validando restricciones de tamaño y obligatoriedad en nombres y disciplinas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Entrenador registrado exitosamente",
                    content = @Content(schema = @Schema(implementation = CoachResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o formatos incompatibles (Validación fallida)", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PostMapping
    public ResponseEntity<CoachResponseDto> create(@Valid @RequestBody CoachRequestDto coach) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(coach));
    }
    @Operation(summary = "Actualizar un entrenador existente", description = "Modifica los datos personales, especialidades o certificaciones de un entrenador buscando por su ID único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entrenador actualizado exitosamente",
                    content = @Content(schema = @Schema(implementation = CoachResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Cuerpo de la solicitud mal formado o validación fallida", content = @Content),
            @ApiResponse(responseCode = "404", description = "No se encontró el entrenador solicitado para actualizar", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<CoachResponseDto> update(@PathVariable Long id,
                                                   @Valid @RequestBody CoachRequestDto coach) {
        CoachResponseDto updated = service.update(id, coach);
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updated);
    }
    @Operation(summary = "Eliminar un entrenador por ID", description = "Remueve permanentemente el perfil del entrenador de la base de datos del sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Entrenador eliminado de forma exitosa (Sin contenido)", content = @Content),
            @ApiResponse(responseCode = "404", description = "El entrenador solicitado no existe en el sistema", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (service.delete(id)){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

}
