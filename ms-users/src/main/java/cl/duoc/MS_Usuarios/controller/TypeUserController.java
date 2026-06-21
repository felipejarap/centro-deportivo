package cl.duoc.MS_Usuarios.controller;

import cl.duoc.MS_Usuarios.dto.TypeUserRequestDto;
import cl.duoc.MS_Usuarios.dto.TypeUserResponseDto;
import cl.duoc.MS_Usuarios.service.TypeUserService;
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
@RequiredArgsConstructor
@RequestMapping("/api/v1/type-users")
@Tag(name = "Tipos de Usuario (Roles)", description = "Endpoints para administrar los roles y categorías de usuarios del sistema")

public class TypeUserController {
        private final TypeUserService service;

    @Operation(summary = "Obtener todos los tipos de usuario", description = "Recupera una lista completa con todas las categorías y roles de usuarios registrados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de roles recuperado exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<TypeUserResponseDto>> findAll() {

        return ResponseEntity.ok(service.findAll());
    }
    @Operation(summary = "Buscar tipo de usuario por ID", description = "Recupera la información y el formato del rol de Spring Security mediante su identificador único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tipo de usuario encontrado con éxito",
                    content = @Content(schema = @Schema(implementation = TypeUserResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "No se encontró ninguna categoría con el ID proporcionado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<TypeUserResponseDto> findById(@PathVariable Long id) {

        TypeUserResponseDto typeUser = service.findById(id);
        if (typeUser == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(typeUser);
    }
    @Operation(summary = "Registrar un nuevo tipo de usuario", description = "Crea una nueva categoría o rol en el sistema validando que el nombre no sea nulo ni vacío.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tipo de usuario registrado exitosamente",
                    content = @Content(schema = @Schema(implementation = TypeUserResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o faltantes (Validación fallida)", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PostMapping
    public ResponseEntity<TypeUserResponseDto> save (@Valid @RequestBody TypeUserRequestDto type) {
        TypeUserResponseDto addType = service.create(type);
        return ResponseEntity.status(HttpStatus.CREATED).body(addType);
    }
    @Operation(summary = "Actualizar un tipo de usuario existente", description = "Modifica el nombre o las propiedades de un rol buscando por su ID único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tipo de usuario actualizado exitosamente",
                    content = @Content(schema = @Schema(implementation = TypeUserResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Cuerpo de la solicitud mal formado o validación fallida", content = @Content),
            @ApiResponse(responseCode = "404", description = "No se encontró el tipo de usuario solicitado para actualizar", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<TypeUserResponseDto> update(@PathVariable Long id, @Valid @RequestBody TypeUserRequestDto type) {
        TypeUserResponseDto updateType = service.update(id,type);
        if(updateType == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updateType);
    }
    @Operation(summary = "Eliminar un tipo de usuario por ID", description = "Remueve permanentemente la categoría de la base de datos del sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tipo de usuario eliminado de forma exitosa (Sin contenido)", content = @Content),
            @ApiResponse(responseCode = "404", description = "La categoría solicitada no existe en el sistema", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<TypeUserResponseDto> delete(@PathVariable Long id) {
        boolean delType = service.deleteById(id);
        if (delType) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
