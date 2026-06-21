package cl.Duoc.MS_Entrenadores.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Modelo de entrada requerido para registrar o actualizar un entrenador en el sistema")
public class CoachRequestDto {


    @NotBlank(message = "El nombre no puede estar en blanco")
    @Size(min = 4,max = 20, message = "El nombre debe tener entre 4 y 20 caracteres")
    @Schema(description = "Primer y segundo nombre del entrenador", example = "Cristian Alejandro")
    private String name;
    @NotBlank(message = "El apellido no debe estar en blanco")
    @Schema(description = "Apellido paterno del entrenador", example = "Perez")
    private String paternalSurname;
    @Schema(description = "Apellido materno del entrenador (Opcional)", example = "Gomez")
    private String maternalSurname;
    @NotBlank(message = "La especialidad no debe estar ne blanco")
    @Schema(description = "Área deportiva de especialización principal", example = "Crossfit / Levantamiento Olímpico")
    private String specialty;
    @NotBlank(message = "La certificacion no debe estar en blanco")
    @Schema(description = "Certificaciones oficiales o títulos que avalan su conocimiento", example = "Certificación CrossFit Level 2 / Preparador Físico Duoc UC")
    private String certification;
}
