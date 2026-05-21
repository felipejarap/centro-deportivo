package cl.duoc.ms_rutinaEjercicio.controller;

import cl.duoc.ms_rutinaEjercicio.dto.RutinaEjercicioRequestDto;
import cl.duoc.ms_rutinaEjercicio.dto.RutinaEjercicioResponseDto;
import cl.duoc.ms_rutinaEjercicio.service.RutinaEjercicioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rutinas")
@RequiredArgsConstructor
public class RutinaEjercicioController {

    private static final Logger log = LoggerFactory.getLogger(RutinaEjercicioController.class);

    private final RutinaEjercicioService service;

    @GetMapping
    public ResponseEntity<List<RutinaEjercicioResponseDto>> findAll() {
        log.info("GET /api/v1/rutinas - Listando todas las rutinas de ejercicio");
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RutinaEjercicioResponseDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/rutinas/{} - Buscando rutina por id", id);
        RutinaEjercicioResponseDto rutina = service.findById(id);
        if (rutina == null) {
            log.warn("GET /api/v1/rutinas/{} - No encontrada, respondiendo 404", id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(rutina);
    }

    @PostMapping
    public ResponseEntity<RutinaEjercicioResponseDto> create(
            @Valid @RequestBody RutinaEjercicioRequestDto rutina) {
        log.info("POST /api/v1/rutinas - Solicitud de creación: idUsuario={}, idEntrenador={}, nombre={}",
                rutina.getIdUsuario(), rutina.getIdEntrenador(), rutina.getNombre());
        RutinaEjercicioResponseDto created = service.create(rutina);
        log.info("POST /api/v1/rutinas - Rutina creada con idRutina={}", created.getIdRutina());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RutinaEjercicioResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody RutinaEjercicioRequestDto rutina) {
        log.info("PUT /api/v1/rutinas/{} - Solicitud de actualización: idUsuario={}, idEntrenador={}",
                id, rutina.getIdUsuario(), rutina.getIdEntrenador());
        RutinaEjercicioResponseDto updated = service.update(id, rutina);
        if (updated == null) {
            log.warn("PUT /api/v1/rutinas/{} - No encontrada para actualizar, respondiendo 404", id);
            return ResponseEntity.notFound().build();
        }
        log.info("PUT /api/v1/rutinas/{} - Actualización exitosa", id);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/rutinas/{} - Solicitud de eliminación", id);
        if (service.delete(id)) {
            log.info("DELETE /api/v1/rutinas/{} - Eliminada exitosamente", id);
            return ResponseEntity.noContent().build();
        }
        log.warn("DELETE /api/v1/rutinas/{} - No encontrada para eliminar, respondiendo 404", id);
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/by-user/{user-id}")
    public ResponseEntity<List<RutinaEjercicioResponseDto>> findByUserId(
            @PathVariable("user-id") Long userId) {
        log.info("GET /api/v1/rutinas/by-user/{} - Buscando rutinas por usuario", userId);
        try {
            List<RutinaEjercicioResponseDto> result = service.findByUserId(userId);
            log.info("GET /api/v1/rutinas/by-user/{} - Resultado: {} registros", userId,
                    result != null ? result.size() : 0);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.warn("GET /api/v1/rutinas/by-user/{} - Usuario no encontrado, respondiendo 404: {}",
                    userId, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/by-entrenador/{entrenador-id}")
    public ResponseEntity<List<RutinaEjercicioResponseDto>> findByEntrenadorId(
            @PathVariable("entrenador-id") Long entrenadorId) {
        log.info("GET /api/v1/rutinas/by-entrenador/{} - Buscando rutinas por entrenador", entrenadorId);
        try {
            List<RutinaEjercicioResponseDto> result = service.findByEntrenadorId(entrenadorId);
            log.info("GET /api/v1/rutinas/by-entrenador/{} - Resultado: {} registros", entrenadorId,
                    result != null ? result.size() : 0);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.warn("GET /api/v1/rutinas/by-entrenador/{} - Entrenador no encontrado, respondiendo 404: {}",
                    entrenadorId, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/by-objetivo/{objetivo}")
    public ResponseEntity<List<RutinaEjercicioResponseDto>> findByObjetivo(
            @PathVariable String objetivo) {
        log.info("GET /api/v1/rutinas/by-objetivo/{} - Buscando rutinas por objetivo", objetivo);
        List<RutinaEjercicioResponseDto> result = service.findByObjetivo(objetivo);
        log.info("GET /api/v1/rutinas/by-objetivo/{} - Resultado: {} registros", objetivo, result.size());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/activas/by-user/{user-id}")
    public ResponseEntity<List<RutinaEjercicioResponseDto>> findActivasByUserId(
            @PathVariable("user-id") Long userId) {
        log.info("GET /api/v1/rutinas/activas/by-user/{} - Buscando rutinas activas por usuario", userId);
        try {
            List<RutinaEjercicioResponseDto> result = service.findActivasByUserId(userId);
            log.info("GET /api/v1/rutinas/activas/by-user/{} - Resultado: {} registros activos", userId,
                    result != null ? result.size() : 0);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.warn("GET /api/v1/rutinas/activas/by-user/{} - Usuario no encontrado, respondiendo 404: {}",
                    userId, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}