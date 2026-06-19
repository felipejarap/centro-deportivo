package cl.duoc.ms.asistencia.dto;

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
public class AssistanceRequestDto {

    @NotNull(message = "El id de usuario es requerido")
    private Long idUser;

    @NotNull(message = "El id de classe es requerido")
    private Long idClasse;

    @NotBlank(message = "La hora de llegada no puede estar vacía")
    private String arrivalTime;

    @NotNull(message = "El campo asistio es requerido")
    private Boolean assist;
}
