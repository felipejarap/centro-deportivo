package cl.duoc.MS_Usuarios.controller;

import cl.duoc.MS_Usuarios.dto.UserRequestDto;
import cl.duoc.MS_Usuarios.dto.UserResponseDto;
import cl.duoc.MS_Usuarios.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Gestión de Usuarios", description = "Endpoints para la creación, actualización, consulta y eliminación de usuarios del centro deportivo")

public class UserController {

    private final UserService service;

    @Operation(summary = "Obtener todos los usuarios", description = "Recupera una lista completa con todos los usuarios registrados en el sistema junto a sus respectivos detalles y roles.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de usuarios recuperado exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> findAll()
    {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Buscar usuarios por tipo de rol", description = "Recupera un listado de usuarios filtrado según el identificador único del tipo de usuario (TypeUser).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuarios filtrados recuperados exitosamente"),
            @ApiResponse(responseCode = "404", description = "No se encontraron usuarios para el tipo especificado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/by-type/{typeUserId}")
    public ResponseEntity<List<UserResponseDto>> findByTypeUserId(@PathVariable Long typeUserId) {
        return ResponseEntity.ok(service.findByTypeUserId(typeUserId));
    }
    @Operation(summary = "Buscar usuario por ID", description = "Recupera la información pública y detallada de un usuario específico mediante su identificador único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado con éxito",
                    content = @Content(schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "No se encontró ningún usuario con el ID proporcionado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> findById(@PathVariable Long id){
        UserResponseDto user = service.findById(id);
        if(user == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }
    @Operation(summary = "Registrar un nuevo usuario", description = "Crea un usuario en el sistema validando unicidad, correos válidos y tamaños de campos definidos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario registrado exitosamente",
                    content = @Content(schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o formatos incompatibles (Validación fallida)", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PostMapping
    public ResponseEntity<UserResponseDto> create(@Valid @RequestBody UserRequestDto user){
        UserResponseDto newUser = service.create(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }
    @Operation(summary = "Actualizar un usuario existente", description = "Modifica los datos personales o el rol de un usuario buscando por su ID único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente",
                    content = @Content(schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Cuerpo de la solicitud mal formado o validación fallida", content = @Content),
            @ApiResponse(responseCode = "404", description = "No se encontró el usuario solicitado para actualizar", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> update(@PathVariable Long id, @Valid @RequestBody UserRequestDto user){
        UserResponseDto updatedUser = service.update(id, user);
        if(updatedUser == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedUser);
    }
    @Operation(summary = "Eliminar un usuario por ID", description = "Remueve permanentemente el registro del usuario de la base de datos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuario eliminado de forma exitosa (Sin contenido)", content = @Content),
            @ApiResponse(responseCode = "404", description = "El usuario solicitado no existe en el sistema", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        boolean deleted = service.delete(id);
        if(deleted){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }


}
