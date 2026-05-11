package duoc.cl.ms_classes.controller;

import duoc.cl.ms_classes.dto.ClasseRequestDto;
import duoc.cl.ms_classes.dto.ClasseResponseDto;
import duoc.cl.ms_classes.dto.TypeClassesResponseDto;
import duoc.cl.ms_classes.service.ClasseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/classes")
public class ClasseController {

    private final ClasseService service;

    @GetMapping
    public ResponseEntity<List<ClasseResponseDto>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClasseResponseDto> findById(@PathVariable Long id) {
        ClasseResponseDto classe = service.findById(id);

        if (classe == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(classe);
    }

    @PostMapping
    public ResponseEntity<ClasseResponseDto> create(@Valid @RequestBody ClasseRequestDto classe) {
        ClasseResponseDto addClasse = service.create(classe);
        return ResponseEntity.status(HttpStatus.CREATED).body(addClasse);
    }

    @PutMapping("/{id}")

    public ResponseEntity<ClasseResponseDto> update(@PathVariable Long id, @Valid @RequestBody ClasseRequestDto classe) {
        ClasseResponseDto updateClasse = service.update(id, classe);

        if (updateClasse == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updateClasse);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ClasseResponseDto> delete(@PathVariable Long id) {
        boolean delClasse = service.deleteById(id);
        if (delClasse) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

}
