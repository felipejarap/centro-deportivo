package duoc.cl.ms_classes.controller;

import duoc.cl.ms_classes.dto.ClasseRequestDto;
import duoc.cl.ms_classes.dto.ClasseResponseDto;
import duoc.cl.ms_classes.dto.TypeClassesResponseDto;
import duoc.cl.ms_classes.service.ClasseService;
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
@RequestMapping("/api/v1/classes")
@Tag(name = "Clases", description = "Endpoints para la programación de horarios, asignación de disciplinas y control de cupos")
public class ClasseController {

    private final ClasseService service;
    @Operation(summary = "Obtener todas las clases", description = "Recupera una lista con todas las sesiones deportivas programadas en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de clases recuperado exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<ClasseResponseDto>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }
    @Operation(summary = "Buscar clase por ID", description = "Recupera la información detallada de una clase específica mediante su identificador único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Clase encontrada con éxito",
                    content = @Content(schema = @Schema(implementation = ClasseResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "No se encontró ninguna clase con el ID proporcionado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<ClasseResponseDto> findById(@PathVariable Long id) {
        ClasseResponseDto classe = service.findById(id);

        if (classe == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(classe);
    }

    @Operation(summary = "Programar una nueva clase", description = "Registra una sesión deportiva en un horario específico validando capacidades y cupos iniciales.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Clase programada exitosamente",
                    content = @Content(schema = @Schema(implementation = ClasseResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o formatos de fecha incorrectos", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PostMapping
    public ResponseEntity<ClasseResponseDto> create(@Valid @RequestBody ClasseRequestDto classe) {
        ClasseResponseDto addClasse = service.create(classe);
        return ResponseEntity.status(HttpStatus.CREATED).body(addClasse);
    }


    @Operation(summary = "Actualizar una clase existente", description = "Modifica los horarios o la capacidad de una clase deportiva buscando por su ID único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Clase actualizada exitosamente",
                    content = @Content(schema = @Schema(implementation = ClasseResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Cuerpo de la solicitud mal formado o validación fallida", content = @Content),
            @ApiResponse(responseCode = "404", description = "No se encontró la clase para actualizar", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<ClasseResponseDto> update(@PathVariable Long id, @Valid @RequestBody ClasseRequestDto classe) {
        ClasseResponseDto updateClasse = service.update(id, classe);

        if (updateClasse == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updateClasse);

    }
    @Operation(summary = "Eliminar una clase por ID", description = "Remueve permanentemente la programación de una clase de la base de datos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Clase eliminada de forma exitosa (Sin contenido)", content = @Content),
            @ApiResponse(responseCode = "404", description = "La clase solicitada no existe", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ClasseResponseDto> delete(@PathVariable Long id) {
        boolean delClasse = service.deleteById(id);
        if (delClasse) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

}
