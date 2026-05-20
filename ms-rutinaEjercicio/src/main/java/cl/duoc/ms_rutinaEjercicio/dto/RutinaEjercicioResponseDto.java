package cl.duoc.ms_rutinaEjercicio.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RutinaEjercicioResponseDto {
    private Long idRutina;
    private Long idUsuario;
    private Long idEntrenador;
    private String nombre;
    private String descripcion;
    private String objetivo;
    private Double pesoRegistrado;
    private Double marcaPersonal;
    private String fechaAsignacion;
    private Boolean activa;

    private UserResponseDto usuario;
    private EntrenadorResponseDto entrenador;
}
