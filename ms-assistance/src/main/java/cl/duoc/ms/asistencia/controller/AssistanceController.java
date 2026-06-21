package cl.duoc.ms.asistencia.controller;
import cl.duoc.ms.asistencia.dto.AssistanceRequestDto;
import cl.duoc.ms.asistencia.dto.AssistanceResponseDto;
import cl.duoc.ms.asistencia.service.AssistanceService;
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
@RequestMapping("/api/v1/assistances")
@RequiredArgsConstructor
@Tag(name = "Gestión de Asistencia", description = "Endpoints para el control de acceso, marcación de horarios y confirmación de presencia de alumnos")
public class AssistanceController {

    private static final Logger log = LoggerFactory.getLogger(AssistanceController.class);

    private final AssistanceService service;
    @Operation(summary = "Obtener todos los registros de asistencia", description = "Recupera una lista con todos los marcajes e ingresos a clases registrados en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de asistencia recuperado exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<AssistanceResponseDto>> findAll() {
        log.info("GET /api/v1/asistencias - Listando todas las asistencias");
        return ResponseEntity.ok(service.findAll());
    }
    @Operation(summary = "Buscar asistencia por ID", description = "Recupera la información de una marcación específica de ingreso mediante su identificador único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Asistencia encontrada con éxito",
                    content = @Content(schema = @Schema(implementation = AssistanceResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "No se encontró ningún registro con el ID proporcionado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<AssistanceResponseDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/assistances/{} - Buscando asistencia por id", id);
        AssistanceResponseDto assistance = service.findById(id);
        if (assistance == null) {
            log.warn("GET /api/v1/assistances/{} - No encontrada, respondiendo 404", id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assistance);
    }
    @Operation(summary = "Registrar un nuevo marcaje de asistencia", description = "Registra el ingreso físico de un alumno a una sesión, validando horas y referencias de ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Asistencia registrada de forma exitosa",
                    content = @Content(schema = @Schema(implementation = AssistanceResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o faltantes (Validación fallida)", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PostMapping
    public ResponseEntity<AssistanceResponseDto> create(
            @Valid @RequestBody AssistanceRequestDto assistance) {
        log.info("POST /api/v1/assistances - Solicitud de creación: idUsuario={}, idClasse={}",
                assistance.getIdUser(), assistance.getIdClasse());
        AssistanceResponseDto created = service.create(assistance);
        log.info("POST /api/v1/assistances - Asistencia creada con idAsistencia={}", created.getIdAssistance());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    @Operation(summary = "Actualizar un registro de asistencia", description = "Modifica los estados de presencia o el horario de llegada de un registro buscando por su ID único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registro de asistencia actualizado exitosamente",
                    content = @Content(schema = @Schema(implementation = AssistanceResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Cuerpo de la solicitud mal formado o validación de campos fallida", content = @Content),
            @ApiResponse(responseCode = "404", description = "No se encontró la marcación de asistencia para actualizar", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<AssistanceResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody AssistanceRequestDto assistance) {
        log.info("PUT /api/v1/assistances/{} - Solicitud de actualización: idUsuario={}, idClasse={}",
                id, assistance.getIdUser(), assistance.getIdClasse());
        AssistanceResponseDto updated = service.update(id, assistance);
        if (updated == null) {
            log.warn("PUT /api/v1/assistances/{} - No encontrada para actualizar, respondiendo 404", id);
            return ResponseEntity.notFound().build();
        }
        log.info("PUT /api/v1/assistances/{} - Actualización exitosa", id);
        return ResponseEntity.ok(updated);
    }
    @Operation(summary = "Eliminar asistencia por ID", description = "Remueve de forma permanente el registro de marcaje de la base de datos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Asistencia eliminada con éxito (Sin contenido)", content = @Content),
            @ApiResponse(responseCode = "404", description = "El registro de asistencia solicitado no existe", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/assistances/{} - Solicitud de eliminación", id);
        if (service.delete(id)) {
            log.info("DELETE /api/v1/assistances/{} - Eliminada exitosamente", id);
            return ResponseEntity.noContent().build();
        }
        log.warn("DELETE /api/v1/assistances/{} - No encontrada para eliminar, respondiendo 404", id);
        return ResponseEntity.notFound().build();
    }
    @Operation(summary = "Listar asistencias por ID de Usuario", description = "Recupera la lista completa de asistencias y ausencias de un alumno específico mediante su ID de ms-users.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Historial de asistencia del usuario recuperado con éxito"),
            @ApiResponse(responseCode = "404", description = "No se encontraron marcajes de asistencia para el usuario indicado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/by-user/{userId}")
    public ResponseEntity<List<AssistanceResponseDto>> findByUserId(
            @PathVariable("userId") Long userId) {
        log.info("GET /api/v1/assistances/by-user/{} - Buscando asistencias por usuario", userId);
        try {
            List<AssistanceResponseDto> result = service.findByUserId(userId);
            log.info("GET /api/v1/assistances/by-user/{} - Resultado: {} registros", userId,
                    result != null ? result.size() : 0);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.warn("GET /api/v1/assistances/by-user/{} - Usuario no encontrado, respondiendo 404: {}",
                    userId, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    @Operation(summary = "Listar asistencias por ID de Clase", description = "Recupera el listado total de alumnos que asistieron o faltaron a una clase específica de ms-classes.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de asistencia de la clase recuperado con éxito"),
            @ApiResponse(responseCode = "404", description = "No se encontraron marcajes de asistencia vinculados a la clase indicada", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/by-classe/{classeId}")
    public ResponseEntity<List<AssistanceResponseDto>> findByClasseId(
            @PathVariable("classeId") Long classeId) {
        log.info("GET /api/v1/assistances/by-classe/{} - Buscando asistencias por clase", classeId);
        try {
            List<AssistanceResponseDto> result = service.findByClasseId(classeId);
            log.info("GET /api/v1/assistances/by-classe/{} - Resultado: {} registros", classeId,
                    result != null ? result.size() : 0);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.warn("GET /api/v1/assistances/by-classe/{} - Clase no encontrada, respondiendo 404: {}",
                    classeId, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}