package cl.duoc.ms.asistencia.controller;
import cl.duoc.ms.asistencia.dto.AssistanceRequestDto;
import cl.duoc.ms.asistencia.dto.AssistanceResponseDto;
import cl.duoc.ms.asistencia.service.AssistanceService;
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
public class AssistanceController {

    private static final Logger log = LoggerFactory.getLogger(AssistanceController.class);

    private final AssistanceService service;

    @GetMapping
    public ResponseEntity<List<AssistanceResponseDto>> findAll() {
        log.info("GET /api/v1/asistencias - Listando todas las asistencias");
        return ResponseEntity.ok(service.findAll());
    }

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

    @PostMapping
    public ResponseEntity<AssistanceResponseDto> create(
            @Valid @RequestBody AssistanceRequestDto assistance) {
        log.info("POST /api/v1/assistances - Solicitud de creación: idUsuario={}, idClasse={}",
                assistance.getIdUser(), assistance.getIdClasse());
        AssistanceResponseDto created = service.create(assistance);
        log.info("POST /api/v1/assistances - Asistencia creada con idAsistencia={}", created.getIdAssistance());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

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