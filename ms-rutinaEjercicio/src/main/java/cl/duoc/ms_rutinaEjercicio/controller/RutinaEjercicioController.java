package cl.duoc.ms_rutinaEjercicio.controller;

import cl.duoc.ms_rutinaEjercicio.dto.RutinaEjercicioRequestDto;
import cl.duoc.ms_rutinaEjercicio.dto.RutinaEjercicioResponseDto;
import cl.duoc.ms_rutinaEjercicio.service.RutinaEjercicioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rutinas")
@RequiredArgsConstructor
public class RutinaEjercicioController {

    private final RutinaEjercicioService service;

    @GetMapping
    public ResponseEntity<List<RutinaEjercicioResponseDto>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RutinaEjercicioResponseDto> findById(@PathVariable Long id) {
        RutinaEjercicioResponseDto rutina = service.findById(id);
        if (rutina == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(rutina);
    }

    @PostMapping
    public ResponseEntity<RutinaEjercicioResponseDto> create(
            @Valid @RequestBody RutinaEjercicioRequestDto rutina) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.create(rutina));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RutinaEjercicioResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody RutinaEjercicioRequestDto rutina) {
        RutinaEjercicioResponseDto updated = service.update(id, rutina);
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (service.delete(id)) return ResponseEntity.noContent().build();
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/by-user/{user-id}")
    public ResponseEntity<List<RutinaEjercicioResponseDto>> findByUserId(
            @PathVariable("user-id") Long userId) {
        try {
            return ResponseEntity.ok(service.findByUserId(userId));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/by-entrenador/{entrenador-id}")
    public ResponseEntity<List<RutinaEjercicioResponseDto>> findByEntrenadorId(
            @PathVariable("entrenador-id") Long entrenadorId) {
        try {
            return ResponseEntity.ok(service.findByEntrenadorId(entrenadorId));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/by-objetivo/{objetivo}")
    public ResponseEntity<List<RutinaEjercicioResponseDto>> findByObjetivo(
            @PathVariable String objetivo) {
        return ResponseEntity.ok(service.findByObjetivo(objetivo));
    }

    @GetMapping("/activas/by-user/{user-id}")
    public ResponseEntity<List<RutinaEjercicioResponseDto>> findActivasByUserId(
            @PathVariable("user-id") Long userId) {
        try {
            return ResponseEntity.ok(service.findActivasByUserId(userId));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
