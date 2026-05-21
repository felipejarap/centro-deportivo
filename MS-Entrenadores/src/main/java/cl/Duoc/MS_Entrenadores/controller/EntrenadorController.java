package cl.Duoc.MS_Entrenadores.controller;
import cl.Duoc.MS_Entrenadores.dto.EntrenadorRequestDto;
import cl.Duoc.MS_Entrenadores.dto.EntrenadorResponseDto;
import cl.Duoc.MS_Entrenadores.service.EntrenadorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/entrenadores")
@RequiredArgsConstructor
public class EntrenadorController {

    private final EntrenadorService service;

    @GetMapping
    public ResponseEntity<List<EntrenadorResponseDto>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }
    @GetMapping("/{id}")
    public ResponseEntity<EntrenadorResponseDto> findById(@PathVariable Long id) {
        EntrenadorResponseDto entrenador = service.findById(id);
        if (entrenador == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(entrenador);
    }

    @PostMapping
    public ResponseEntity<EntrenadorResponseDto> create(@Valid @RequestBody EntrenadorRequestDto entrenador) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(entrenador));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntrenadorResponseDto> update(@PathVariable Long id,
                                                        @Valid @RequestBody EntrenadorRequestDto entrenador) {
        EntrenadorResponseDto updated = service.update(id, entrenador);
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (service.delete(id)){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

}
