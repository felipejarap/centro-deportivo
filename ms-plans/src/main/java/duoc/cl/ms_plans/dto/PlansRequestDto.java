package duoc.cl.ms_plans.dto;

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
public class PlansRequestDto {

    private Long id;
    @NotBlank(message = "El name no debe de estar en blanco")
    private String name;
    @PositiveOrZero(message = "Debe ser numero positivo")
    private double price;
    @PositiveOrZero(message = "Debe ser numero positivo")
    private int durationDays;
}
