package cl.Duoc.MS_Entrenadores.controller;
import cl.Duoc.MS_Entrenadores.dto.CoachRequestDto;
import cl.Duoc.MS_Entrenadores.dto.CoachResponseDto;
import cl.Duoc.MS_Entrenadores.service.CoachService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/coaches")
@RequiredArgsConstructor
public class CoachController {

    private final CoachService service;

    @GetMapping
    public ResponseEntity<List<CoachResponseDto>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }
    @GetMapping("/{id}")
    public ResponseEntity<CoachResponseDto> findById(@PathVariable Long id) {
        CoachResponseDto coach = service.findById(id);
        if (coach == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(coach);
    }

    @PostMapping
    public ResponseEntity<CoachResponseDto> create(@Valid @RequestBody CoachRequestDto coach) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(coach));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CoachResponseDto> update(@PathVariable Long id,
                                                   @Valid @RequestBody CoachRequestDto coach) {
        CoachResponseDto updated = service.update(id, coach);
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
