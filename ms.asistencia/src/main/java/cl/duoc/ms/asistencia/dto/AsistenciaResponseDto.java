package cl.duoc.ms.asistencia.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AsistenciaResponseDto {

    private Long idAsistencia;
    private Long idUsuario;
    private Long idClasse;
    private String horaLlegada;
    private Boolean asistio;

    private UserResponseDto usuario;
    private ClasseResponseDto classe;
}
