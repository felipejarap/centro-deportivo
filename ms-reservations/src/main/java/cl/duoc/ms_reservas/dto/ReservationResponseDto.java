package cl.duoc.ms_reservas.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Modelo de respuesta que representa los datos públicos y consolidados de una reserva")
public class ReservationResponseDto {
    @Schema(description = "Identificador único de la reserva registrado en la base de datos", example = "500")
    private Long idReservation;
    @Schema(description = "Identificador único del usuario/alumno", example = "45")
    private Long idUser;
    @Schema(description = "Identificador único de la clase deportiva", example = "12")
    private Long idClasse;
    @Schema(description = "Identificador único del entrenador asignado", example = "2")
    private Long idCoach;
    @Schema(description = "Fecha confirmada para la reserva (Formato YYYY-MM-DD)", example = "2026-06-25")
    private LocalDateTime reservationDate;
    @Schema(description = "Estado actual de la reserva en el sistema", example = "CONFIRMADA")
    private String reservationStatus;

    // Datos enriquecidos desde otros microservicios
    @Schema(description = "Información pública complementaria del alumno (Proveniente de MS_Usuarios)")
    private UserResponseDto user;
    @Schema(description = "Información pública complementaria de la clase programada (Proveniente de ms-classes)")
    private ClasseResponseDto classe;
    @Schema(description = "Información pública complementaria del entrenador (Proveniente de MS_Entrenadores)")
    private CoachResponseDto coach;
}
