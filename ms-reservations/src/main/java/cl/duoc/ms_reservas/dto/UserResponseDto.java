package cl.duoc.ms_reservas.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
    private Long id;
    private String username;
    private String paternalSurname;
    private String maternalSurname;
    private String email;
    private String phone;
    private TypeUserResponseDto typeUser;
}

