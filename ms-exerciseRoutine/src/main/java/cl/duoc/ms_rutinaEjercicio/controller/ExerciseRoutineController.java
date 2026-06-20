package cl.duoc.ms_rutinaEjercicio.controller;

import cl.duoc.ms_rutinaEjercicio.dto.ExerciseRoutineRequestDto;
import cl.duoc.ms_rutinaEjercicio.dto.ExerciseRoutineResponseDto;
import cl.duoc.ms_rutinaEjercicio.service.ExerciseRoutineService;
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
public class ExerciseRoutineController {

    private static final Logger log = LoggerFactory.getLogger(ExerciseRoutineController.class);

    private final ExerciseRoutineService service;

    @GetMapping
    public ResponseEntity<List<ExerciseRoutineResponseDto>> findAll() {
        log.info("GET /api/v1/routines - Listando todas las rutinas de ejercicio");
        return ResponseEntity.ok(service.findAll());
    }

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

    @PostMapping
    public ResponseEntity<ExerciseRoutineResponseDto> create(
            @Valid @RequestBody ExerciseRoutineRequestDto rutina) {
        log.info("POST /api/v1/routines - Solicitud de creación: idUser={}, idCoach={}, name={}",
                rutina.getIdUser(), rutina.getIdCoach(), rutina.getName());
        ExerciseRoutineResponseDto created = service.create(rutina);
        log.info("POST /api/v1/routines - Rutina creada con idRoutine={}", created.getIdRoutine());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

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

    @GetMapping("/by-objective/{objective}")
    public ResponseEntity<List<ExerciseRoutineResponseDto>> findByObjective(
            @PathVariable String objective) {
        log.info("GET /api/v1/routines/by-objective/{} - Buscando rutinas por objetivo", objective);
        List<ExerciseRoutineResponseDto> result = service.findByObjective(objective);
        log.info("GET /api/v1/routines/by-objective/{} - Resultado: {} registros", objective, result.size());
        return ResponseEntity.ok(result);
    }

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