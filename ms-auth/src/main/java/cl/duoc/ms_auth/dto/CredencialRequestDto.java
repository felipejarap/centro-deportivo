package cl.duoc.ms_auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CredencialRequestDto {
    @NotBlank(message = "El username no puede estar vacío")
    private String username;

    @NotBlank(message = "El password no puede estar vacío")
    @Size(min = 6, message ="El password debe tener un minimo de 6 caracteres")
    private String password;

    @NotNull(message = "El id de usuario es requerido")
    private Long idUser;


}
