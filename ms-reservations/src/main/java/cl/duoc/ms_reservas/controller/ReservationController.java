package cl.duoc.ms_reservas.controller;
import cl.duoc.ms_reservas.dto.ReservationRequestDto;
import cl.duoc.ms_reservas.dto.ReservationResponseDto;
import cl.duoc.ms_reservas.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reservations")
@RequiredArgsConstructor
@Tag(name = "Gestión de Reservas", description = "Endpoints para coordinar agendas, cupos de clases y asignación de bloques deportivos para los alumnos")

public class ReservationController {

    private static final Logger log = LoggerFactory.getLogger(ReservationController.class);

    private final ReservationService service;

    @Operation(summary = "Obtener todas las reservas", description = "Recupera una lista con el historial completo de todas las solicitudes de reservas registradas en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de reservas recuperado exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<ReservationResponseDto>> findAll() {
        log.info("GET /api/v1/reservations - Listando todas las reservas");
        return ResponseEntity.ok(service.findAll());
    }
    @Operation(summary = "Buscar reserva por ID", description = "Recupera los datos consolidados y el estado de una reserva específica mediante su identificador único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reserva encontrada con éxito",
                    content = @Content(schema = @Schema(implementation = ReservationResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "No se encontró ningún registro con el ID proporcionado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponseDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/reservations/{} - Buscando reserva por id", id);
        ReservationResponseDto reserva = service.findById(id);
        if (reserva == null) {
            log.warn("GET /api/v1/reservations/{} - No encontrada, respondiendo 404", id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(reserva);
    }
    @Operation(summary = "Registrar una nueva reserva", description = "Crea una solicitud de cupo para una clase deportiva validando la existencia del usuario, la sesión y el entrenador.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Reserva generada y confirmada exitosamente",
                    content = @Content(schema = @Schema(implementation = ReservationResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o parámetros faltantes (Validación fallida)", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PostMapping
    public ResponseEntity<ReservationResponseDto> create(
            @Valid @RequestBody ReservationRequestDto reservations) {
        log.info("POST /api/v1/reservations - Solicitud de creación: idUser={}, idClasse={}, idCoach={}",
                reservations.getIdUser(), reservations.getIdClasse(), reservations.getIdCoach());
        ReservationResponseDto created = service.create(reservations);
        log.info("POST /api/v1/reservations - Reserva creada con idReservations={}", created.getIdReservation());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    @Operation(summary = "Actualizar una reserva existente", description = "Permite modificar el estado de la reserva (ej: CANCELADA) o reprogramar la fecha buscando por su ID único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reserva actualizada de forma exitosa",
                    content = @Content(schema = @Schema(implementation = ReservationResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Cuerpo de la solicitud mal formado o validación de campos errónea", content = @Content),
            @ApiResponse(responseCode = "404", description = "No se encontró el registro de reserva solicitado para actualizar", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<ReservationResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody ReservationRequestDto reservations) {
        log.info("PUT /api/v1/reservations/{} - Solicitud de actualización: idUser={}, idClasse={}, idCoach={}",
                id, reservations.getIdUser(), reservations.getIdClasse(), reservations.getIdCoach());
        ReservationResponseDto updated = service.update(id, reservations);
        if (updated == null) {
            log.warn("PUT /api/v1/reservations/{} - No encontrada para actualizar, respondiendo 404", id);
            return ResponseEntity.notFound().build();
        }
        log.info("PUT /api/v1/reservations/{} - Actualización exitosa", id);
        return ResponseEntity.ok(updated);
    }
    @Operation(summary = "Cancelar/Eliminar una reserva por ID", description = "Remueve de forma permanente el registro de la reserva del sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Reserva eliminada con éxito (Sin contenido)", content = @Content),
            @ApiResponse(responseCode = "404", description = "La reserva solicitada no existe en el sistema", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/reservations/{} - Solicitud de eliminación", id);
        if (service.delete(id)) {
            log.info("DELETE /api/v1/reservations/{} - Eliminada exitosamente", id);
            return ResponseEntity.noContent().build();
        }
        log.warn("DELETE /api/v1/reservations/{} - No encontrada para eliminar, respondiendo 404", id);
        return ResponseEntity.notFound().build();
    }
    @Operation(summary = "Listar reservas por ID de Usuario", description = "Recupera todas las agendas e historial de reservas de un alumno específico mediante su ID de MS_Usuarios.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Historial de reservas del usuario recuperado con éxito"),
            @ApiResponse(responseCode = "404", description = "No se encontraron reservas para el usuario especificado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/by-user/{userId}")
    public ResponseEntity<List<ReservationResponseDto>> findByUserId(@PathVariable Long userId) throws Exception {
        log.info("GET /api/v1/reservations/by-user/{} - Buscando reservas por usuario", userId);
        try {
            List<ReservationResponseDto> result = service.findByUserId(userId);
            log.info("GET /api/v1/reservations/by-user/{} - Resultado: {} registros", userId,
                    result != null ? result.size() : 0);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.warn("GET /api/v1/reservations/by-user/{} - Usuario no encontrado, respondiendo 404: {}",
                    userId, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    @Operation(summary = "Listar reservas por ID de Entrenador", description = "Recupera las reservas y citas vigentes de clases que están bajo la tutela de un profesor de MS_Entrenadores.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de reservas del entrenador recuperado con éxito"),
            @ApiResponse(responseCode = "404", description = "No se encontraron reservas vinculadas al entrenador indicado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/by-coach/{coachId}")
    public ResponseEntity<List<ReservationResponseDto>> findByCoachId(@PathVariable Long coachId) throws Exception {
        log.info("GET /api/v1/reservations/by-coach/{} - Buscando reservas por entrenador", coachId);
        try {
            List<ReservationResponseDto> result = service.findByCoachId(coachId);
            log.info("GET /api/v1/reservations/by-coach/{} - Resultado: {} registros", coachId,
                    result != null ? result.size() : 0);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.warn("GET /api/v1/reservations/by-coach/{} - Entrenador no encontrado, respondiendo 404: {}",
                    coachId, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    @Operation(summary = "Listar reservas por ID de Clase", description = "Recupera la nómina completa de cupos reservados para una sesión o clase programada de ms-classes.")
    @ApiResponses(value =
            {@ApiResponse(responseCode = "200", description = "Nómina de reservas para la clase recuperada con éxito"),
                    @ApiResponse(responseCode = "404", description = "No se encontraron reservas asociadas a la clase indicada", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)})
    @GetMapping("/by-classe/{classeId}")
    public ResponseEntity<List<ReservationResponseDto>> findByClasseId(@PathVariable Long classeId) throws Exception {
        log.info("GET /api/v1/reservations/by-classe/{} - Buscando reservas por clase", classeId);
        try {
            List<ReservationResponseDto> result = service.findByClasseId(classeId);
            log.info("GET /api/v1/reservations/by-classe/{} - Resultado: {} registros", classeId,
                    result != null ? result.size() : 0);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.warn("GET /api/v1/reservations/by-classe/{} - Clase no encontrada, respondiendo 404: {}",
                    classeId, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}