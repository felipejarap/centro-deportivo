package cl.duoc.ms_reservas.dto;

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
    private String appaterno;
    private String apmaterno;
    private String email;
    private String phone;
    private TypeUserResponseDto typeUser;
}

