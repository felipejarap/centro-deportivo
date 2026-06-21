package duoc.cl.ms_classes.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
@Schema(description = "Modelo de entrada requerido para registrar o actualizar una disciplina/tipo de clase")
public class TypeClassesRequestDto {
    @Schema(description = "Identificador único (Solo requerido para actualizaciones con PUT)", example = "2")
    Long id;
    @NotBlank(message = "El name no puede estar en blanco")
    @Schema(description = "Nombre descriptivo de la disciplina deportiva", example = "Crossfit")
    String name;
}
