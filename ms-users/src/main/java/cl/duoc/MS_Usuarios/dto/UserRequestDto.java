package cl.duoc.MS_Usuarios.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter @AllArgsConstructor @NoArgsConstructor
public class UserRequestDto {

    @NotBlank(message = "El username no puede estar vacio")
    @Size(min = 4,max = 20, message = "el username debe tener entre 4 y 20 caracteres")
    private String username;

    @NotBlank(message = "El apellido paterno es requerido")
    private String paternalSurname;
    @NotBlank(message = "El apellido materno es requerido")
    private String maternalSurname;

    @NotBlank(message = "El email no puede estar vacio")
    @Email(message = "Debe proporcionar un formato de email valido")
    private String email;
    private String phone;
    @NotNull(message = "El tipo de usuario es requerido")
    private Long typeUserId;



}
