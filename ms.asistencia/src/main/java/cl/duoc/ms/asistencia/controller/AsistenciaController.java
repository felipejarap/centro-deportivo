package cl.duoc.ms.asistencia.controller;
import cl.duoc.ms.asistencia.dto.AsistenciaRequestDto;
import cl.duoc.ms.asistencia.dto.AsistenciaResponseDto;
import cl.duoc.ms.asistencia.service.AsistenciaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/asistencias")
@RequiredArgsConstructor
public class AsistenciaController {

    private static final Logger log = LoggerFactory.getLogger(AsistenciaController.class);

    private final AsistenciaService service;

    @GetMapping
    public ResponseEntity<List<AsistenciaResponseDto>> findAll() {
        log.info("GET /api/v1/asistencias - Listando todas las asistencias");
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AsistenciaResponseDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/asistencias/{} - Buscando asistencia por id", id);
        AsistenciaResponseDto asistencia = service.findById(id);
        if (asistencia == null) {
            log.warn("GET /api/v1/asistencias/{} - No encontrada, respondiendo 404", id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(asistencia);
    }

    @PostMapping
    public ResponseEntity<AsistenciaResponseDto> create(
            @Valid @RequestBody AsistenciaRequestDto asistencia) {
        log.info("POST /api/v1/asistencias - Solicitud de creación: idUsuario={}, idClasse={}",
                asistencia.getIdUsuario(), asistencia.getIdClasse());
        AsistenciaResponseDto created = service.create(asistencia);
        log.info("POST /api/v1/asistencias - Asistencia creada con idAsistencia={}", created.getIdAsistencia());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AsistenciaResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody AsistenciaRequestDto asistencia) {
        log.info("PUT /api/v1/asistencias/{} - Solicitud de actualización: idUsuario={}, idClasse={}",
                id, asistencia.getIdUsuario(), asistencia.getIdClasse());
        AsistenciaResponseDto updated = service.update(id, asistencia);
        if (updated == null) {
            log.warn("PUT /api/v1/asistencias/{} - No encontrada para actualizar, respondiendo 404", id);
            return ResponseEntity.notFound().build();
        }
        log.info("PUT /api/v1/asistencias/{} - Actualización exitosa", id);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/asistencias/{} - Solicitud de eliminación", id);
        if (service.delete(id)) {
            log.info("DELETE /api/v1/asistencias/{} - Eliminada exitosamente", id);
            return ResponseEntity.noContent().build();
        }
        log.warn("DELETE /api/v1/asistencias/{} - No encontrada para eliminar, respondiendo 404", id);
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/by-user/{userId}")
    public ResponseEntity<List<AsistenciaResponseDto>> findByUserId(
            @PathVariable("userId") Long userId) {
        log.info("GET /api/v1/asistencias/by-user/{} - Buscando asistencias por usuario", userId);
        try {
            List<AsistenciaResponseDto> result = service.findByUserId(userId);
            log.info("GET /api/v1/asistencias/by-user/{} - Resultado: {} registros", userId,
                    result != null ? result.size() : 0);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.warn("GET /api/v1/asistencias/by-user/{} - Usuario no encontrado, respondiendo 404: {}",
                    userId, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/by-classe/{classe-id}")
    public ResponseEntity<List<AsistenciaResponseDto>> findByClasseId(
            @PathVariable("classe-id") Long classeId) {
        log.info("GET /api/v1/asistencias/by-classe/{} - Buscando asistencias por clase", classeId);
        try {
            List<AsistenciaResponseDto> result = service.findByClasseId(classeId);
            log.info("GET /api/v1/asistencias/by-classe/{} - Resultado: {} registros", classeId,
                    result != null ? result.size() : 0);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.warn("GET /api/v1/asistencias/by-classe/{} - Clase no encontrada, respondiendo 404: {}",
                    classeId, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}