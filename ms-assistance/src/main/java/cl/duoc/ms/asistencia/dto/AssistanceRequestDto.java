package cl.duoc.ms.asistencia.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Modelo de entrada requerido para registrar el control de asistencia de un alumno a una clase")
public class AssistanceRequestDto {

    @NotNull(message = "El id de usuario es requerido")
    @Schema(description = "Identificador único del usuario/alumno (Proveniente de MS_Usuarios)", example = "45")
    private Long idUser;
    @NotNull(message = "El id de classe es requerido")
    @Schema(description = "Identificador único de la clase programada (Proveniente de ms-classes)", example = "12")
    private Long idClasse;
    @NotBlank(message = "La hora de llegada no puede estar vacía")
    @Schema(description = "Hora exacta del marcaje de ingreso en formato de 24 horas", example = "09:05")
    private String arrivalTime;
    @NotNull(message = "El campo asistio es requerido")
    @Schema(description = "Estado de confirmación física (true = Presente / false = Ausente)", example = "true")
    private Boolean assist;
}
