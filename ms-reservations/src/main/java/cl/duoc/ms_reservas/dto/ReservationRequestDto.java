package cl.duoc.ms_reservas.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Modelo de entrada requerido para solicitar o modificar una reserva de clase deportiva")

public class ReservationRequestDto {

    @NotNull(message = "El id de usuario es requerido")
    @Schema(description = "Identificador único del usuario/alumno (Proveniente de MS_Usuarios)", example = "45")
    private Long idUser;

    @NotNull(message = "El id de clase es requerido")
    @Schema(description = "Identificador único de la clase que se desea reservar (Proveniente de ms-classes)", example = "12")
    private Long idClasse;

    @NotNull(message = "El id de entrenador es requerido")
    @Schema(description = "Identificador único del entrenador asignado (Proveniente de MS_Entrenadores)", example = "2")
    private Long idCoach;

    @NotNull(message = "La fecha de reserva no puede estar vacía")
    @Schema(description = "Fecha programada para asistir a la sesión deportiva (Formato YYYY-MM-DD)", example = "2026-06-25")
    private LocalDateTime reservationDate;

    @NotBlank(message = "El estado de reserva no puede estar vacío")
    @Schema(description = "Estado inicial o de actualización de la solicitud (CONFIRMADA, PENDIENTE, CANCELADA)", example = "CONFIRMADA")
    private String reservationStatus;
}
