package cl.duoc.MS_Usuarios.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TypeUserResponseDto {

    private Long id;
    private String name;

    /** Nombre del rol Spring Security derivado del tipo de usuario. */
    public String getRoleName() {
        return name != null ? "ROLE_" + name.toUpperCase() : null;
    }
}
