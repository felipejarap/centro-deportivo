package duoc.cl.ms_plans.controller;

import duoc.cl.ms_plans.dto.PlansRequestDto;
import duoc.cl.ms_plans.dto.PlansResponseDto;
import duoc.cl.ms_plans.service.PlansService;
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
@RequestMapping("/api/v1/plans")
@Tag(name = "Gestión de Planes", description = "Endpoints para la administración de las ofertas de membresías y suscripciones")
public class PlansController {
    private final PlansService service;

    @Operation(summary = "Obtener todos los planes", description = "Recupera una lista completa con todos los planes de suscripción registrados en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de planes recuperado exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<PlansResponseDto>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Buscar plan por ID", description = "Recupera la información detallada de un plan de suscripción específico mediante su identificador único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Plan encontrado con éxito",
                    content = @Content(schema = @Schema(implementation = PlansResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "No se encontró ningún plan con el ID proporcionado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<PlansResponseDto> findById(@PathVariable Long id) {
        PlansResponseDto plan = service.findById(id);

        if (plan == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(plan);
    }

    @Operation(summary = "Registrar un nuevo plan", description = "Crea una membresía o plan de suscripción en el sistema validando montos y duraciones.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Plan registrado exitosamente",
                    content = @Content(schema = @Schema(implementation = PlansResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o faltantes", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PostMapping
    public ResponseEntity<PlansResponseDto> create(@Valid @RequestBody PlansRequestDto plan) {
        PlansResponseDto addPlan = service.create(plan);
        return ResponseEntity.status(HttpStatus.CREATED).body(addPlan);
    }

    @Operation(summary = "Actualizar un plan existente", description = "Modifica las tarifas, nombres o plazos de un plan buscando por su ID único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Plan actualizado exitosamente",
                    content = @Content(schema = @Schema(implementation = PlansResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Cuerpo de la solicitud mal formado o validación fallida", content = @Content),
            @ApiResponse(responseCode = "404", description = "No se encontró el plan solicitado para actualizar", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<PlansResponseDto> update(@PathVariable Long id, @Valid @RequestBody PlansRequestDto plan) {
        PlansResponseDto updatePlan = service.update(id, plan);
        if (updatePlan == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatePlan);
    }

    @Operation(summary = "Eliminar un plan por ID", description = "Remueve permanentemente el plan de suscripción de la base de datos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Plan eliminado de forma exitosa (Sin contenido)", content = @Content),
            @ApiResponse(responseCode = "404", description = "El plan solicitado no existe en el sistema", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<PlansResponseDto> delete(@PathVariable Long id) {
        boolean delPlan = service.deleteById(id);
        if (delPlan) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }


}
