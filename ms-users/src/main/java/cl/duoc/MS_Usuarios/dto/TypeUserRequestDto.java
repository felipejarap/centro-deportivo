package cl.duoc.MS_Usuarios.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Modelo de entrada requerido para registrar o actualizar un tipo de usuario o rol")
public class TypeUserRequestDto {

    @NotBlank(message = "El nombre no puede estar en blanco")
    @Schema(description = "Nombre de la categoría o rol que se creará en el sistema", example = "Entrenador")
    private String name;
}
