package cl.duoc.MS_Usuarios.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Modelo de respuesta que representa el tipo de rol asignada a un usuario")
public class TypeUserResponseDto {

    @Schema(description = "Identificador único del tipo de usuario en la base de datos", example = "1")
    private Long id;
    @Schema(description = "Nombre descriptivo de la categoría o rol de usuario", example = "Administrador")
    private String name;

    /** Nombre del rol Spring Security derivado del tipo de usuario. */
    @Schema(description = "Nombre formateado del rol compatible con Spring Security (Autogenerado)", example = "ROLE_ADMINISTRADOR")
    public String getRoleName() {
        return name != null ? "ROLE_" + name.toUpperCase() : null;
    }
}
