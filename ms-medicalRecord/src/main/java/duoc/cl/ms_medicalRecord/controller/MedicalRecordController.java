package duoc.cl.ms_medicalRecord.controller;

import duoc.cl.ms_medicalRecord.dto.MedicalRecordRequestDto;
import duoc.cl.ms_medicalRecord.dto.MedicalRecordResponseDto;
import duoc.cl.ms_medicalRecord.service.MedicalRecordService;
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
@RequestMapping("/api/v1/medical-records")
@RequiredArgsConstructor
@Tag(name = "Gestión de Fichas Médicas", description = "Endpoints para administrar preexistencias, alergias e historial de salud de los alumnos")

public class MedicalRecordController {

    private static final Logger log = LoggerFactory.getLogger(MedicalRecordController.class);

    private final MedicalRecordService service;
    @Operation(summary = "Obtener todas las fichas médicas", description = "Recupera una lista con el historial completo de todas las fichas de salud registradas en el centro.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de fichas médicas recuperado exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<MedicalRecordResponseDto>> findAll() {
        log.info("GET /api/v1/medical-records - Listando todos los registros médicos");
        return ResponseEntity.ok(service.findAll());
    }
    @Operation(summary = "Buscar ficha médica por ID", description = "Recupera la información de salud detallada de un registro mediante su identificador único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ficha médica encontrada con éxito",
                    content = @Content(schema = @Schema(implementation = MedicalRecordResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "No se encontró ningún registro con el ID proporcionado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<MedicalRecordResponseDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/medical-records/{} - Buscando registro médico por id", id);
        MedicalRecordResponseDto medicalRecord = service.findById(id);
        if (medicalRecord == null) {
            log.warn("GET /api/v1/medical-records/{} - No encontrado, respondiendo 404", id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(medicalRecord);
    }
    @Operation(summary = "Buscar fichas médicas por ID de Usuario", description = "Recupera los datos de salud asociados a un identificador único de usuario de ms-users.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registros médicos del usuario recuperados exitosamente"),
            @ApiResponse(responseCode = "404", description = "No se encontraron fichas de salud vinculadas al usuario indicado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/by-user/{userId}")
    public ResponseEntity<List<MedicalRecordResponseDto>> findByUserId(@PathVariable Long userId) throws Exception {
        log.info("GET /api/v1/medical-records/by-user/{} - Buscando registros médicos por usuario", userId);
        try {
            List<MedicalRecordResponseDto> result = service.findByUserId(userId);
            log.info("GET /api/v1/medical-records/by-user/{} - Resultado: {} registros", userId,
                    result != null ? result.size() : 0);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.warn("GET /api/v1/medical-records/by-user/{} - Usuario no encontrado, respondiendo 404: {}",
                    userId, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    @Operation(summary = "Registrar una nueva ficha médica", description = "Crea el expediente de salud de un alumno validando que las preexistencias y el ID de usuario no sean nulos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Ficha médica registrada exitosamente",
                    content = @Content(schema = @Schema(implementation = MedicalRecordResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o faltantes (Validación fallida)", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PostMapping
    public ResponseEntity<MedicalRecordResponseDto> create(@Valid @RequestBody MedicalRecordRequestDto medicalRecord) {
        log.info("POST /api/v1/medical-records - Solicitud de creación: userId={}", medicalRecord.getUserId());
        MedicalRecordResponseDto addMedicalRecord = service.create(medicalRecord);
        log.info("POST /api/v1/medical-records - Registro médico creado con id={}", addMedicalRecord.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(addMedicalRecord);
    }
    @Operation(summary = "Actualizar una ficha médica existente", description = "Modifica los datos clínicos, alergias o el centro emisor buscando por su ID único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ficha médica actualizada exitosamente",
                    content = @Content(schema = @Schema(implementation = MedicalRecordResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Cuerpo de la solicitud mal formado o validación fallida", content = @Content),
            @ApiResponse(responseCode = "404", description = "No se encontró la ficha de salud solicitada para actualizar", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<MedicalRecordResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody MedicalRecordRequestDto medicalRecord) {
        log.info("PUT /api/v1/medical-records/{} - Solicitud de actualización: userId={}", id, medicalRecord.getUserId());
        MedicalRecordResponseDto updatedMedicalRecord = service.update(id, medicalRecord);
        if (updatedMedicalRecord == null) {
            log.warn("PUT /api/v1/medical-records/{} - No encontrado para actualizar, respondiendo 404", id);
            return ResponseEntity.notFound().build();
        }
        log.info("PUT /api/v1/medical-records/{} - Actualización exitosa", id);
        return ResponseEntity.ok(updatedMedicalRecord);
    }
    @Operation(summary = "Eliminar una ficha médica por ID", description = "Remueve permanentemente el registro de salud de la base de datos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Ficha médica eliminada con éxito (Sin contenido)", content = @Content),
            @ApiResponse(responseCode = "404", description = "La ficha médica solicitada no existe", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/medical-records/{} - Solicitud de eliminación", id);
        boolean deleted = service.deleteById(id);
        if (deleted) {
            log.info("DELETE /api/v1/medical-records/{} - Eliminado exitosamente", id);
            return ResponseEntity.noContent().build();
        }
        log.warn("DELETE /api/v1/medical-records/{} - No encontrado para eliminar, respondiendo 404", id);
        return ResponseEntity.notFound().build();
    }
}