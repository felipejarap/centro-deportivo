package cl.duoc.ms_auth.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CredencialResponseDto {
    private Long idCredencial;
    private String username;
    private Long idUsuario;
    private Boolean activo;
}
