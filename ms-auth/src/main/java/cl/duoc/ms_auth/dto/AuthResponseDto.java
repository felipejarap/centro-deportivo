package cl.duoc.ms_auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter @AllArgsConstructor @NoArgsConstructor
public class AuthResponseDto {

    private String token;
    private String username;
    private Long idUsuario;
    private String rol;
    private String mensaje;
}
