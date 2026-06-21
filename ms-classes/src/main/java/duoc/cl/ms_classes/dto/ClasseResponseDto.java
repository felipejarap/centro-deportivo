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
@Schema(description = "Modelo de respuesta con la información pública y detallada de una clase")
public class ClasseResponseDto {

    @NotNull
    @Schema(description = "Identificador único de la clase", example = "1")
    Long id;
    @Schema(description = "Fecha y hora de inicio de la sesión", example = "2026-06-25T09:00:00")
    LocalDateTime startDate;
    @Schema(description = "Fecha y hora de término de la sesión", example = "2026-06-25T10:30:00")
    LocalDateTime endDate;
    @PositiveOrZero
    @Schema(description = "Capacidad máxima total permitida", example = "30")
    Integer maximumCapacity;
    @PositiveOrZero
    @Schema(description = "Cupos libres disponibles actualmente para reservar", example = "15")
    Integer spotsAvailable;
    @NotNull
    @Schema(description = "Detalles de la disciplina o tipo de clase asignada")
    TypeClasses typeClasse;
}
