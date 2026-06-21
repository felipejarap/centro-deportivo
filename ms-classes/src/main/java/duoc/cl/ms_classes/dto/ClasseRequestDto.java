package duoc.cl.ms_classes.dto;

import duoc.cl.ms_classes.model.TypeClasses;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter @Getter @AllArgsConstructor @NoArgsConstructor
@Schema(description = "Modelo de entrada requerido para registrar o actualizar una clase deportiva")

public class ClasseRequestDto {

    @Schema(description = "Identificador único de la clase (Solo requerido para actualizaciones)", example = "1")
    Long id;
    @Schema(description = "Fecha y hora de inicio de la sesión deportiva", example = "2026-06-25T09:00:00")
    LocalDateTime startDate;
    @Schema(description = "Fecha y hora de término de la sesión deportiva", example = "2026-06-25T10:30:00")
    LocalDateTime endDate;
    @PositiveOrZero(message = "Debe ser numero positivo")
    @Schema(description = "Capacidad máxima de alumnos permitidos en la sala", example = "30")
    Integer maximumCapacity;
    @PositiveOrZero(message = "Debe ser numero positivo")
    @Schema(description = "Cupos disponibles actualmente para recibir reservas", example = "15")
    Integer spotsAvailable;
    @NotNull(message = "type classes no debe ser nulo")
    @Schema(description = "Identificador único del tipo de clase o disciplina asociada", example = "2")
    TypeClasses typeClasse;
}
