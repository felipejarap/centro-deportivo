package duoc.cl.ms_plans.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter @AllArgsConstructor @NoArgsConstructor
public class PlansResponseDto {

    @NotNull
    @Schema(description = "Identificador único del plan ", example = "1")
    private Long id;
    @NotBlank
    @Schema(description = "Nombre oficial del plan de suscripción", example = "Plan Mensual Black")
    private String name;
    @PositiveOrZero
    @Schema(description = "Costo o tarifa del plan", example = "29990.00")
    private double price;
    @PositiveOrZero
    @Schema(description = "Cantidad de días de acceso que otorga el plan", example = "30")
    private int durationDays;

}
