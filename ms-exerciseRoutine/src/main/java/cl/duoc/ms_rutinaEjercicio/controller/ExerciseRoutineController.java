package cl.duoc.ms_rutinaEjercicio.controller;

import cl.duoc.ms_rutinaEjercicio.dto.ExerciseRoutineRequestDto;
import cl.duoc.ms_rutinaEjercicio.dto.ExerciseRoutineResponseDto;
import cl.duoc.ms_rutinaEjercicio.service.ExerciseRoutineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/routines")
@RequiredArgsConstructor
@Tag(name = "Gestión de Rutinas de Ejercicio", description = "Endpoints para la asignación de planes físicos, marcas personales y seguimiento de objetivos deportivos")

public class ExerciseRoutineController {

    private static final Logger log = LoggerFactory.getLogger(ExerciseRoutineController.class);

    private final ExerciseRoutineService service;
    @Operation(summary = "Obtener todas las rutinas", description = "Recupera una lista completa con todos los planes y rutinas de ejercicio registrados en la base de datos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de rutinas recuperado exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<ExerciseRoutineResponseDto>> findAll() {
        log.info("GET /api/v1/routines - Listando todas las rutinas de ejercicio");
        return ResponseEntity.ok(service.findAll());
    }
    @Operation(summary = "Buscar rutina por ID", description = "Recupera la información técnica detallada de una rutina de ejercicios mediante su identificador único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rutina encontrada con éxito",
                    content = @Content(schema = @Schema(implementation = ExerciseRoutineResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "No se encontró ninguna rutina con el ID proporcionado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<ExerciseRoutineResponseDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/routines/{} - Buscando rutina por id", id);
        ExerciseRoutineResponseDto rutina = service.findById(id);
        if (rutina == null) {
            log.warn("GET /api/v1/routines/{} - No encontrada, respondiendo 404", id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(rutina);
    }
    @Operation(summary = "Asignar una nueva rutina", description = "Registra un plan físico personalizado para un alumno asociándolo a un entrenador calificado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Rutina creada y asignada exitosamente",
                    content = @Content(schema = @Schema(implementation = ExerciseRoutineResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o faltantes (Validación fallida)", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PostMapping
    public ResponseEntity<ExerciseRoutineResponseDto> create(
            @Valid @RequestBody ExerciseRoutineRequestDto rutina) {
        log.info("POST /api/v1/routines - Solicitud de creación: idUser={}, idCoach={}, name={}",
                rutina.getIdUser(), rutina.getIdCoach(), rutina.getName());
        ExerciseRoutineResponseDto created = service.create(rutina);
        log.info("POST /api/v1/routines - Rutina creada con idRoutine={}", created.getIdRoutine());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    @Operation(summary = "Actualizar una rutina existente", description = "Modifica los ejercicios, marcas personales, metas o pesos corporales de una rutina buscando por su ID único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rutina de ejercicios actualizada exitosamente",
                    content = @Content(schema = @Schema(implementation = ExerciseRoutineResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Cuerpo de la solicitud mal formado o validación de campos errónea", content = @Content),
            @ApiResponse(responseCode = "404", description = "No se encontró el plan de entrenamiento para actualizar", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<ExerciseRoutineResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody ExerciseRoutineRequestDto routine) {
        log.info("PUT /api/v1/routines/{} - Solicitud de actualización: idUser={}, idCoach={}",
                id, routine.getIdUser(), routine.getIdCoach());
        ExerciseRoutineResponseDto updated = service.update(id, routine);
        if (updated == null) {
            log.warn("PUT /api/v1/routines/{} - No encontrada para actualizar, respondiendo 404", id);
            return ResponseEntity.notFound().build();
        }
        log.info("PUT /api/v1/routines/{} - Actualización exitosa", id);
        return ResponseEntity.ok(updated);
    }
    @Operation(summary = "Eliminar rutina por ID", description = "Remueve permanentemente el registro de la rutina de la base de datos del sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Rutina eliminada con éxito (Sin contenido)", content = @Content),
            @ApiResponse(responseCode = "404", description = "La rutina de ejercicios solicitada no existe", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/routines/{} - Solicitud de eliminación", id);
        if (service.delete(id)) {
            log.info("DELETE /api/v1/routines/{} - Eliminada exitosamente", id);
            return ResponseEntity.noContent().build();
        }
        log.warn("DELETE /api/v1/routines/{} - No encontrada para eliminar, respondiendo 404", id);
        return ResponseEntity.notFound().build();
    }
    @Operation(summary = "Listar rutinas por ID de Usuario", description = "Recupera el historial completo de rutinas asociadas a un alumno mediante su ID de MS_Usuarios.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Historial de rutinas del usuario recuperado con éxito"),
            @ApiResponse(responseCode = "404", description = "No se encontraron planes para el usuario indicado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/by-user/{userId}")
    public ResponseEntity<List<ExerciseRoutineResponseDto>> findByUserId(
            @PathVariable("userId") Long userId) {
        log.info("GET /api/v1/routines/by-user/{} - Buscando rutinas por usuario", userId);
        try {
            List<ExerciseRoutineResponseDto> result = service.findByUserId(userId);
            log.info("GET /api/v1/routines/by-user/{} - Resultado: {} registros", userId,
                    result != null ? result.size() : 0);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.warn("GET /api/v1/routines/by-user/{} - Usuario no encontrado, respondiendo 404: {}",
                    userId, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    @Operation(summary = "Listar rutinas por ID de Entrenador", description = "Recupera el listado total de rutinas creadas o supervisadas por un entrenador específico de MS_Entrenadores.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de rutinas del entrenador recuperado con éxito"),
            @ApiResponse(responseCode = "404", description = "No se encontraron rutinas vinculadas al entrenador indicado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/by-coach/{coachId}")
    public ResponseEntity<List<ExerciseRoutineResponseDto>> findByCoachId(
            @PathVariable("coachId") Long coachId) {
        log.info("GET /api/v1/routines/by-coach/{} - Buscando rutinas por entrenador", coachId);
        try {
            List<ExerciseRoutineResponseDto> result = service.findByCoachId(coachId);
            log.info("GET /api/v1/routines/by-coach/{} - Resultado: {} registros", coachId,
                    result != null ? result.size() : 0);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.warn("GET /api/v1/routines/by-coach/{} - Entrenador no encontrado, respondiendo 404: {}",
                    coachId, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    @Operation(summary = "Listar rutinas por Objetivo", description = "Recupera todas las rutinas que coincidan con un objetivo específico (ej: Hipertrofia, Fuerza).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado filtrado por objetivos recuperado exitosamente"),            @ApiResponse(responseCode = "404", description = "No se encontraron rutinas vinculadas al entrenador indicado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)    })
    @GetMapping("/by-objective/{objective}")
    public ResponseEntity<List<ExerciseRoutineResponseDto>> findByObjective(
            @PathVariable String objective) {
        log.info("GET /api/v1/routines/by-objective/{} - Buscando rutinas por objetivo", objective);
        List<ExerciseRoutineResponseDto> result = service.findByObjective(objective);
        log.info("GET /api/v1/routines/by-objective/{} - Resultado: {} registros", objective, result.size());
        return ResponseEntity.ok(result);
    }
    @Operation(summary = "Obtener rutinas activas de un Usuario", description = "Recupera los planes físicos vigentes y que se encuentran en uso actual por parte del alumno.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rutinas vigentes y activas encontradas con éxito"),
            @ApiResponse(responseCode = "404", description = "No se encontraron planes activos para el usuario especificado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)})
    @GetMapping("/actives/by-user/{userId}")
    public ResponseEntity<List<ExerciseRoutineResponseDto>> findActivasByUserId(
            @PathVariable("userId") Long userId) {
        log.info("GET /api/v1/routines/actives/by-user/{} - Buscando rutinas activas por usuario", userId);
        try {
            List<ExerciseRoutineResponseDto> result = service.findActivesByUserId(userId);
            log.info("GET /api/v1/routines/actives/by-user/{} - Resultado: {} registros activos", userId,
                    result != null ? result.size() : 0);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.warn("GET /api/v1/routines/actives/by-user/{} - Usuario no encontrado, respondiendo 404: {}",
                    userId, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}