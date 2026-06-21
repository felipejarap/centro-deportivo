package duoc.cl.ms_classes.controller;

import duoc.cl.ms_classes.dto.TypeClassesRequestDto;
import duoc.cl.ms_classes.dto.TypeClassesResponseDto;
import duoc.cl.ms_classes.service.TypeClassesService;
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
@RequestMapping("/api/v1/type-classes")
@Tag(name = "Disciplinas", description = "Endpoints para administrar las categorías o disciplinas del centro deportivo")
public class TypeClassesController {
    private final TypeClassesService service;


    @Operation(summary = "Obtener todas las disciplinas", description = "Recupera una lista completa con todos los tipos de clases deportivas registrados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de disciplinas recuperado exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<TypeClassesResponseDto>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Buscar disciplina por ID", description = "Recupera la información detallada de una disciplina deportiva específica mediante su identificador único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Disciplina encontrada exitosamente",
                    content = @Content(schema = @Schema(implementation = TypeClassesResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "No se encontró ninguna disciplina con el ID proporcionado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<TypeClassesResponseDto> findById(@PathVariable Long id) {

        TypeClassesResponseDto typeClass = service.findById(id);
        return ResponseEntity.ok(typeClass);

    }

    @Operation(summary = "Registrar una nueva disciplina", description = "Crea un tipo de clase deportiva en el sistema validando que el nombre cumpla con las restricciones.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Disciplina registrada con éxito",
                    content = @Content(schema = @Schema(implementation = TypeClassesResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos (Validación fallida)", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PostMapping
    public ResponseEntity<TypeClassesResponseDto> save(@Valid @RequestBody TypeClassesRequestDto type) {
        TypeClassesResponseDto addType = service.create(type);
        return ResponseEntity.status(HttpStatus.CREATED).body(addType);
    }


    @Operation(summary = "Actualizar una disciplina existente", description = "Modifica los datos de una disciplina deportiva buscando por su ID único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Disciplina actualizada exitosamente",
                    content = @Content(schema = @Schema(implementation = TypeClassesResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Cuerpo de la solicitud mal formado o datos inválidos", content = @Content),
            @ApiResponse(responseCode = "404", description = "No se encontró el recurso para actualizar", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<TypeClassesResponseDto> update(@PathVariable Long id, @Valid @RequestBody TypeClassesRequestDto type) {
        TypeClassesResponseDto updateType = service.update(id, type);
        if (updateType == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updateType);
    }

    @Operation(summary = "Eliminar una disciplina por ID", description = "Remueve permanentemente una disciplina de la base de datos del sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Disciplina eliminada de forma exitosa", content = @Content),
            @ApiResponse(responseCode = "404", description = "La disciplina solicitada no existe", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<TypeClassesResponseDto> delete(@PathVariable Long id) {
        boolean delType = service.deleteById(id);
        if (delType) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

}
