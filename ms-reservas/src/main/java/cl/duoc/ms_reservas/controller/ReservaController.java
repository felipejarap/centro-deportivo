package cl.duoc.ms_reservas.controller;
import cl.duoc.ms_reservas.dto.ReservaRequestDto;
import cl.duoc.ms_reservas.dto.ReservaResponseDto;
import cl.duoc.ms_reservas.service.ReservaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reservas")
@RequiredArgsConstructor
public class ReservaController {

    private static final Logger log = LoggerFactory.getLogger(ReservaController.class);

    private final ReservaService service;

    @GetMapping
    public ResponseEntity<List<ReservaResponseDto>> findAll() {
        log.info("GET /api/v1/reservas - Listando todas las reservas");
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservaResponseDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/reservas/{} - Buscando reserva por id", id);
        ReservaResponseDto reserva = service.findById(id);
        if (reserva == null) {
            log.warn("GET /api/v1/reservas/{} - No encontrada, respondiendo 404", id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(reserva);
    }

    @PostMapping
    public ResponseEntity<ReservaResponseDto> create(
            @Valid @RequestBody ReservaRequestDto reserva) {
        log.info("POST /api/v1/reservas - Solicitud de creación: idUsuario={}, idClase={}, idEntrenador={}",
                reserva.getIdUsuario(), reserva.getIdClase(), reserva.getIdEntrenador());
        ReservaResponseDto created = service.create(reserva);
        log.info("POST /api/v1/reservas - Reserva creada con idReserva={}", created.getIdReserva());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservaResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody ReservaRequestDto reserva) {
        log.info("PUT /api/v1/reservas/{} - Solicitud de actualización: idUsuario={}, idClase={}, idEntrenador={}",
                id, reserva.getIdUsuario(), reserva.getIdClase(), reserva.getIdEntrenador());
        ReservaResponseDto updated = service.update(id, reserva);
        if (updated == null) {
            log.warn("PUT /api/v1/reservas/{} - No encontrada para actualizar, respondiendo 404", id);
            return ResponseEntity.notFound().build();
        }
        log.info("PUT /api/v1/reservas/{} - Actualización exitosa", id);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/reservas/{} - Solicitud de eliminación", id);
        if (service.delete(id)) {
            log.info("DELETE /api/v1/reservas/{} - Eliminada exitosamente", id);
            return ResponseEntity.noContent().build();
        }
        log.warn("DELETE /api/v1/reservas/{} - No encontrada para eliminar, respondiendo 404", id);
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/by-user/{user-id}")
    public ResponseEntity<List<ReservaResponseDto>> findByUserId(@PathVariable Long UserId) throws Exception {
        log.info("GET /api/v1/reservas/by-user/{} - Buscando reservas por usuario", UserId);
        try {
            List<ReservaResponseDto> result = service.findByUserId(UserId);
            log.info("GET /api/v1/reservas/by-user/{} - Resultado: {} registros", UserId,
                    result != null ? result.size() : 0);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.warn("GET /api/v1/reservas/by-user/{} - Usuario no encontrado, respondiendo 404: {}",
                    UserId, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/by-entrenador/{entrenador-id}")
    public ResponseEntity<List<ReservaResponseDto>> findByEntrenadorId(@PathVariable Long EntrenadorId) throws Exception {
        log.info("GET /api/v1/reservas/by-entrenador/{} - Buscando reservas por entrenador", EntrenadorId);
        try {
            List<ReservaResponseDto> result = service.findByEntrenadorId(EntrenadorId);
            log.info("GET /api/v1/reservas/by-entrenador/{} - Resultado: {} registros", EntrenadorId,
                    result != null ? result.size() : 0);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.warn("GET /api/v1/reservas/by-entrenador/{} - Entrenador no encontrado, respondiendo 404: {}",
                    EntrenadorId, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/by-classe/{classe-id}")
    public ResponseEntity<List<ReservaResponseDto>> findByClasseId(@PathVariable Long ClasseId) throws Exception {
        log.info("GET /api/v1/reservas/by-classe/{} - Buscando reservas por clase", ClasseId);
        try {
            List<ReservaResponseDto> result = service.findByClasseId(ClasseId);
            log.info("GET /api/v1/reservas/by-classe/{} - Resultado: {} registros", ClasseId,
                    result != null ? result.size() : 0);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.warn("GET /api/v1/reservas/by-classe/{} - Clase no encontrada, respondiendo 404: {}",
                    ClasseId, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}