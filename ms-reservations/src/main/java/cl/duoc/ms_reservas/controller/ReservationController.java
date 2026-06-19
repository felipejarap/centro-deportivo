package cl.duoc.ms_reservas.controller;
import cl.duoc.ms_reservas.dto.ReservationRequestDto;
import cl.duoc.ms_reservas.dto.ReservationResponseDto;
import cl.duoc.ms_reservas.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private static final Logger log = LoggerFactory.getLogger(ReservationController.class);

    private final ReservationService service;

    @GetMapping
    public ResponseEntity<List<ReservationResponseDto>> findAll() {
        log.info("GET /api/v1/reservations - Listando todas las reservas");
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponseDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/reservations/{} - Buscando reserva por id", id);
        ReservationResponseDto reserva = service.findById(id);
        if (reserva == null) {
            log.warn("GET /api/v1/reservations/{} - No encontrada, respondiendo 404", id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(reserva);
    }

    @PostMapping
    public ResponseEntity<ReservationResponseDto> create(
            @Valid @RequestBody ReservationRequestDto reservations) {
        log.info("POST /api/v1/reservations - Solicitud de creación: idUser={}, idClasse={}, idCoach={}",
                reservations.getIdUser(), reservations.getIdClasse(), reservations.getIdCoach());
        ReservationResponseDto created = service.create(reservations);
        log.info("POST /api/v1/reservations - Reserva creada con idReservations={}", created.getIdReservation());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservationResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody ReservationRequestDto reservations) {
        log.info("PUT /api/v1/reservations/{} - Solicitud de actualización: idUser={}, idClasse={}, idCoach={}",
                id, reservations.getIdUser(), reservations.getIdClasse(), reservations.getIdCoach());
        ReservationResponseDto updated = service.update(id, reservations);
        if (updated == null) {
            log.warn("PUT /api/v1/reservations/{} - No encontrada para actualizar, respondiendo 404", id);
            return ResponseEntity.notFound().build();
        }
        log.info("PUT /api/v1/reservations/{} - Actualización exitosa", id);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/reservations/{} - Solicitud de eliminación", id);
        if (service.delete(id)) {
            log.info("DELETE /api/v1/reservations/{} - Eliminada exitosamente", id);
            return ResponseEntity.noContent().build();
        }
        log.warn("DELETE /api/v1/reservations/{} - No encontrada para eliminar, respondiendo 404", id);
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/by-user/{userId}")
    public ResponseEntity<List<ReservationResponseDto>> findByUserId(@PathVariable Long userId) throws Exception {
        log.info("GET /api/v1/reservations/by-user/{} - Buscando reservas por usuario", userId);
        try {
            List<ReservationResponseDto> result = service.findByUserId(userId);
            log.info("GET /api/v1/reservations/by-user/{} - Resultado: {} registros", userId,
                    result != null ? result.size() : 0);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.warn("GET /api/v1/reservations/by-user/{} - Usuario no encontrado, respondiendo 404: {}",
                    userId, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/by-coach/{coachId}")
    public ResponseEntity<List<ReservationResponseDto>> findByCoachId(@PathVariable Long coachId) throws Exception {
        log.info("GET /api/v1/reservations/by-coach/{} - Buscando reservas por entrenador", coachId);
        try {
            List<ReservationResponseDto> result = service.findByCoachId(coachId);
            log.info("GET /api/v1/reservations/by-coach/{} - Resultado: {} registros", coachId,
                    result != null ? result.size() : 0);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.warn("GET /api/v1/reservations/by-coach/{} - Entrenador no encontrado, respondiendo 404: {}",
                    coachId, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/by-classe/{classeId}")
    public ResponseEntity<List<ReservationResponseDto>> findByClasseId(@PathVariable Long classeId) throws Exception {
        log.info("GET /api/v1/reservations/by-classe/{} - Buscando reservas por clase", classeId);
        try {
            List<ReservationResponseDto> result = service.findByClasseId(classeId);
            log.info("GET /api/v1/reservations/by-classe/{} - Resultado: {} registros", classeId,
                    result != null ? result.size() : 0);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.warn("GET /api/v1/reservations/by-classe/{} - Clase no encontrada, respondiendo 404: {}",
                    classeId, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}