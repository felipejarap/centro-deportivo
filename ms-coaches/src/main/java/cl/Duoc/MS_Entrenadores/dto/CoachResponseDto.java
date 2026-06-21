package cl.Duoc.MS_Entrenadores.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Modelo de respuesta que representa los datos públicos e informativos de un entrenador")

public class CoachResponseDto {
    @Schema(description = "Identificador único del entrenador registrado en la base de datos", example = "12")
    private Long idCoach;
    @Schema(description = "Nombres oficiales del entrenador", example = "Cristian Alejandro")
    private String name;
    @Schema(description = "Apellido paterno del entrenador", example = "Perez")
    private String paternalSurname;
    @Schema(description = "Apellido materno del entrenador", example = "Gomez")
    private String maternalSurname;
    @Schema(description = "Disciplina o área de especialización deportiva", example = "Crossfit / Levantamiento Olímpico")
    private String specialty;
    @Schema(description = "Certificaciones o títulos oficiales que posee", example = "Certificación CrossFit Level 2 / Preparador Físico Duoc UC")
    private String certification;

}
