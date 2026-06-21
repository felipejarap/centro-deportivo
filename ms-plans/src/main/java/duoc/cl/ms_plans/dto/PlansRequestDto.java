package duoc.cl.ms_plans.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Modelo de entrada requerido para registrar o actualizar un plan de suscripción")
public class PlansRequestDto {

    @Schema(description = "Identificador único del plan (Solo requerido para actualizaciones)", example = "1")
    private Long id;
    @NotBlank(message = "El name no debe de estar en blanco")
    @Schema(description = "Nombre comercial del plan de suscripción", example = "Plan Mensual Black")
    private String name;
    @PositiveOrZero(message = "Debe ser numero positivo")
    @Schema(description = "Precio o valor monetario asignado al plan", example = "29990.00")
    private double price;
    @PositiveOrZero(message = "Debe ser numero positivo")
    @Schema(description = "Duración total de la membresía expresada en días", example = "30")
    private int durationDays;
}
