package cl.duoc.MS_Usuarios.controller;

import cl.duoc.MS_Usuarios.dto.TypeUserRequestDto;
import cl.duoc.MS_Usuarios.dto.TypeUserResponseDto;
import cl.duoc.MS_Usuarios.service.TypeUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/type-users")
public class TypeUserController {
        private final TypeUserService service;

    @GetMapping
    public ResponseEntity<List<TypeUserResponseDto>> findAll() {

        return ResponseEntity.ok(service.findAll());
    }
    @GetMapping("/{id}")
    public ResponseEntity<TypeUserResponseDto> findById(@PathVariable Long id) {

        TypeUserResponseDto typeUser = service.findById(id);
        if (typeUser == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(typeUser);
    }
    @PostMapping
    public ResponseEntity<TypeUserResponseDto> save (@Valid @RequestBody TypeUserRequestDto type) {
        TypeUserResponseDto addType = service.create(type);
        return ResponseEntity.status(HttpStatus.CREATED).body(addType);
    }
    @PutMapping("/{id}")
    public ResponseEntity<TypeUserResponseDto> update(@PathVariable Long id, @Valid @RequestBody TypeUserRequestDto type) {
        TypeUserResponseDto updateType = service.update(id,type);
        if(updateType == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updateType);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TypeUserResponseDto> delete(@PathVariable Long id) {
        boolean delType = service.deleteById(id);
        if (delType) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }






}
