package cl.duoc.MS_Usuarios.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter @AllArgsConstructor @NoArgsConstructor
public class UserResponseDto {

    @NotNull
    private Long id;
    @NotNull
    private String username;
    private String paternalSurname;
    private String maternalSurname;
    @Email
    private String email;
    private String phone;
    private TypeUserResponseDto typeUser;

}
