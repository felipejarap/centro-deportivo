package cl.duoc.ms_subscription.controller;

import cl.duoc.ms_subscription.dto.SubscriptionRequestDto;
import cl.duoc.ms_subscription.dto.SubscriptionResponseDto;
import cl.duoc.ms_subscription.model.Subscription;
import cl.duoc.ms_subscription.service.SubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/subscriptions")
@RequiredArgsConstructor
@Tag(name = "Gestión de Suscripciones", description = "Endpoints para la administración, control de vigencia y asignación de membresías a usuarios")
public class SubscriptionController {
    private final SubscriptionService service;


    @Operation(summary = "Obtener todas las suscripciones", description = "Recupera una lista completa con todas las membresías contratadas registradas en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de suscripciones recuperado exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<SubscriptionResponseDto>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }
    @Operation(summary = "Buscar suscripción por ID", description = "Recupera la información detallada de un registro de suscripción específico mediante su identificador único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Suscripción encontrada con éxito",
                    content = @Content(schema = @Schema(implementation = SubscriptionResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "No se encontró ninguna suscripción con el ID proporcionado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionResponseDto> findById(@PathVariable Long id) {
        SubscriptionResponseDto subscription = service.findById(id);

        if (subscription == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(subscription);
    }

    @Operation(summary = "Buscar suscripciones por ID de Usuario", description = "Recupera el historial o suscripción activa asociada a un identificador único de usuario de ms-users.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Suscripciones del usuario recuperadas exitosamente"),
            @ApiResponse(responseCode = "404", description = "No se encontraron registros de suscripción para el usuario indicado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/by-user/{userId}")
    public ResponseEntity<List<SubscriptionResponseDto>> findByUserId(@PathVariable Long userId) throws Exception {

        try {
            return ResponseEntity.ok(service.findByUserId(userId));

        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    @Operation(summary = "Buscar suscripciones por ID de Plan", description = "Recupera todas las suscripciones de usuarios que pertenecen a un tipo de plan específico de ms-plans.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de usuarios asociados al plan recuperado exitosamente"),
            @ApiResponse(responseCode = "404", description = "No se encontraron suscripciones vinculadas al plan indicado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/by-plans/{plansId}")
    public ResponseEntity<List<SubscriptionResponseDto>> findByPlansId(@PathVariable Long plansId) throws Exception {

        try {
            return ResponseEntity.ok(service.findByPlansId(plansId));

        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Registrar una nueva suscripción", description = "Asigna un plan de membresía a un usuario validando que los identificadores obligatorios no sean nulos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Suscripción registrada con éxito",
                    content = @Content(schema = @Schema(implementation = SubscriptionResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o faltantes (Validación fallida)", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })

    @PostMapping
    public ResponseEntity<SubscriptionResponseDto> create(@Valid @RequestBody SubscriptionRequestDto subscription) {
        SubscriptionResponseDto addSubscription = service.create(subscription);
        return ResponseEntity.status(HttpStatus.CREATED).body(addSubscription);
    }
    @Operation(summary = "Actualizar una suscripción existente", description = "Modifica los estados, vigencias o asociaciones de una membresía buscando por su ID único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Suscripción actualizada de forma exitosa",
                    content = @Content(schema = @Schema(implementation = SubscriptionResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Cuerpo de la solicitud mal formado o datos de entrada incompatibles", content = @Content),
            @ApiResponse(responseCode = "404", description = "No se encontró el registro solicitado para actualizar", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<SubscriptionResponseDto> update(@PathVariable Long id, @Valid @RequestBody SubscriptionRequestDto subscription){
        SubscriptionResponseDto updatedSubscription = service.update(id, subscription);
        if(updatedSubscription == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedSubscription);
    }
    @Operation(summary = "Eliminar una suscripción por ID", description = "Remueve permanentemente el registro de la suscripción de la base de datos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Suscripción eliminada de forma exitosa (Sin contenido)", content = @Content),
            @ApiResponse(responseCode = "404", description = "El registro de suscripción solicitado no existe", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        boolean deleted = service.deleteById(id);
        if(deleted){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

}
