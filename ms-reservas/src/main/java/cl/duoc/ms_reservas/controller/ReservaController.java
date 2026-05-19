package cl.duoc.ms_reservas.controller;

import cl.duoc.ms_reservas.dto.ReservaRequestDto;
import cl.duoc.ms_reservas.dto.ReservaResponseDto;
import cl.duoc.ms_reservas.service.ReservaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reservas")
@RequiredArgsConstructor
public class ReservaController {
    private final ReservaService service;

    @GetMapping
    public ResponseEntity<List<ReservaResponseDto>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservaResponseDto> findById(@PathVariable Long id) {
        ReservaResponseDto reserva = service.findById(id);
        if (reserva == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(reserva);
    }

    @PostMapping
    public ResponseEntity<ReservaResponseDto> create(
            @Valid @RequestBody ReservaRequestDto reserva) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.create(reserva));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservaResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody ReservaRequestDto reserva) {
        ReservaResponseDto updated = service.update(id, reserva);
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (service.delete(id)) return ResponseEntity.noContent().build();
        return ResponseEntity.notFound().build();
    }
}
