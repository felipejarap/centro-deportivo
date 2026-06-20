package cl.duoc.ms_rutinaEjercicio.dto;

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
}
