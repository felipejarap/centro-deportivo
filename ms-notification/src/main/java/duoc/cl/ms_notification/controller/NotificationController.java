package duoc.cl.ms_notification.controller;

import duoc.cl.ms_notification.dto.NotificationRequestDto;
import duoc.cl.ms_notification.dto.NotificationResponseDto;
import duoc.cl.ms_notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@Tag(name = "Gestión de Notificaciones", description = "Endpoints para el envío, auditoría y control de alertas y correos electrónicos del sistema")

public class NotificationController {
    private final NotificationService service;

    @Operation(summary = "Obtener todas las notificaciones", description = "Recupera una lista con el historial completo de todas las alertas y correos procesados por el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Historial de notificaciones recuperado exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<NotificationResponseDto>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }
    @Operation(summary = "Buscar notificación por ID", description = "Recupera los datos de un envío de correo específico mediante su identificador único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notificación encontrada con éxito",
                    content = @Content(schema = @Schema(implementation = NotificationResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "No se encontró ningún registro con el ID proporcionado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<NotificationResponseDto> findById(@PathVariable Long id) {
        NotificationResponseDto dto = service.findById(id);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }
    @Operation(summary = "Despachar una nueva notificación", description = "Solicita el procesamiento y envío de un correo electrónico validando los datos de destino.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notificación despachada y enviada correctamente",
                    content = @Content(schema = @Schema(implementation = String.class, example = "Notificación enviada correctamente a cristian.perez@duocuc.cl"))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o formato de correo erróneo", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno o falla en el servidor SMTP de correo", content = @Content)
    })
    @PostMapping
    public ResponseEntity<?>send(@RequestBody NotificationRequestDto dto)
    {
        try {
               service.send(dto);
            return ResponseEntity.ok("Notificación enviada correctamente a " + dto.getTo());
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error al procesar la notificación: " + e.getMessage());
        }
    }
}
