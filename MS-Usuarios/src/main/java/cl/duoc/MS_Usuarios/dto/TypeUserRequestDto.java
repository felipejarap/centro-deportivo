package cl.duoc.MS_Usuarios.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TypeUserRequestDto {

    @NotBlank(message = "El nombre no puede estar en blanco")
    private String name;
}
