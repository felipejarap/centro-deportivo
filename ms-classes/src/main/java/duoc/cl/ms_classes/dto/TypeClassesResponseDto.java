package duoc.cl.ms_classes.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
@Schema(description = "Modelo de respuesta con la información pública y detallada de un tipo de clase")
public class TypeClassesResponseDto {
    @NotEmpty
    @Schema(description = "Identificador único registrado en la base de datos", example = "2")
    Long id;
    @NotBlank
    @Schema(description = "Nombre oficial de la disciplina deportiva", example = "Crossfit")
    String name;



}
