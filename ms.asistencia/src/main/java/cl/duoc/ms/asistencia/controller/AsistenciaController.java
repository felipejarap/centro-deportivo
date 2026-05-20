package cl.duoc.ms.asistencia.controller;

import cl.duoc.ms.asistencia.dto.AsistenciaRequestDto;
import cl.duoc.ms.asistencia.dto.AsistenciaResponseDto;
import cl.duoc.ms.asistencia.service.AsistenciaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/asistencias")
@RequiredArgsConstructor
public class AsistenciaController {

    private final AsistenciaService service;

    @GetMapping
    public ResponseEntity<List<AsistenciaResponseDto>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AsistenciaResponseDto> findById(@PathVariable Long id) {
        AsistenciaResponseDto asistencia = service.findById(id);
        if (asistencia == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(asistencia);
    }

    @PostMapping
    public ResponseEntity<AsistenciaResponseDto> create(
            @Valid @RequestBody AsistenciaRequestDto asistencia) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.create(asistencia));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AsistenciaResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody AsistenciaRequestDto asistencia) {
        AsistenciaResponseDto updated = service.update(id, asistencia);
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (service.delete(id)) return ResponseEntity.noContent().build();
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/by-user/{userId}")
    public ResponseEntity<List<AsistenciaResponseDto>> findByUserId(
            @PathVariable("userId") Long userId) {
        try {
            return ResponseEntity.ok(service.findByUserId(userId));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/by-classe/{classe-id}")
    public ResponseEntity<List<AsistenciaResponseDto>> findByClasseId(
            @PathVariable("classe-id") Long classeId) {
        try {
            return ResponseEntity.ok(service.findByClasseId(classeId));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
