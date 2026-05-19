package cl.duoc.ms_auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioResponseDto {

    @JsonProperty("id")
    private Long idUser;
    private String username;
    private String appaterno;
    private String apmaterno;
    private String email;
    private String phone;
    private TypeUserResponseDto typeUser;
}
