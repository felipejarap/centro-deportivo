package duoc.cl.ms_classes.controller;

import duoc.cl.ms_classes.dto.TypeClassesRequestDto;
import duoc.cl.ms_classes.dto.TypeClassesResponseDto;
import duoc.cl.ms_classes.service.TypeClassesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/type-classes")
public class TypeClassesController {
    private final TypeClassesService service;

    @GetMapping
    public ResponseEntity<List<TypeClassesResponseDto>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TypeClassesResponseDto> findById(@PathVariable Long id) {

        TypeClassesResponseDto typeClass = service.findById(id);
        return ResponseEntity.ok(typeClass);

    }

    @PostMapping
    public ResponseEntity<TypeClassesResponseDto> save(@Valid @RequestBody TypeClassesRequestDto type) {
        TypeClassesResponseDto addType = service.create(type);
        return ResponseEntity.status(HttpStatus.CREATED).body(addType);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TypeClassesResponseDto> update(@PathVariable Long id, @Valid @RequestBody TypeClassesRequestDto type) {
        TypeClassesResponseDto updateType = service.update(id, type);
        if (updateType == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updateType);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TypeClassesResponseDto> delete(@PathVariable Long id) {
        boolean delType = service.deleteById(id);
        if (delType) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

}
