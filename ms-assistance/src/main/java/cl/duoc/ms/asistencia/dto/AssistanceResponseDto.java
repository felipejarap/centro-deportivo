package cl.duoc.ms.asistencia.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Modelo de respuesta que representa el registro oficial de asistencia de un alumno")
public class AssistanceResponseDto {

    @Schema(description = "Identificador único del registro de asistencia en la base de datos", example = "150")
    private Long idAssistance;
    @Schema(description = "Identificador único del usuario/alumno", example = "45")
    private Long idUser;
    @Schema(description = "Identificador único de la clase deportiva", example = "12")
    private Long idClasse;
    @Schema(description = "Hora de marcaje e ingreso validada por el sistema", example = "09:05")
    private String arrivalTime;
    @Schema(description = "Confirmación final de asistencia (true = Presente / false = Ausente)", example = "true")
    private Boolean assist;

    @Schema(description = "Información pública complementaria del alumno")
    private UserResponseDto user;
    @Schema(description = "Información pública complementaria de la clase programada")
    private ClasseResponseDto classe;
}
