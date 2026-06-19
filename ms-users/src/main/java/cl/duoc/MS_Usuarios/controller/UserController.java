package cl.duoc.MS_Usuarios.controller;

import cl.duoc.MS_Usuarios.dto.UserRequestDto;
import cl.duoc.MS_Usuarios.dto.UserResponseDto;
import cl.duoc.MS_Usuarios.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> findAll()
    {
        return ResponseEntity.ok(service.findAll());
    }


    @GetMapping("/by-type/{typeUserId}")
    public ResponseEntity<List<UserResponseDto>> findByTypeUserId(@PathVariable Long typeUserId) {
        return ResponseEntity.ok(service.findByTypeUserId(typeUserId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> findById(@PathVariable Long id){
        UserResponseDto user = service.findById(id);
        if(user == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> create(@Valid @RequestBody UserRequestDto user){
        UserResponseDto newUser = service.create(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> update(@PathVariable Long id, @Valid @RequestBody UserRequestDto user){
        UserResponseDto updatedUser = service.update(id, user);
        if(updatedUser == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedUser);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        boolean deleted = service.delete(id);
        if(deleted){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }


}
