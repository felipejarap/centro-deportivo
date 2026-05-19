package cl.duoc.ms_reservas.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReservaResponseDto {

    private Long idReserva;
    private Long idUsuario;
    private Long idClase;
    private Long idEntrenador;
    private String fechaReserva;
    private String estadoReserva;

    // Datos enriquecidos desde otros microservicios
    private UserResponseDto usuario;
    private ClasseResponseDto clase;
    private EntrenadorResponseDto entrenador;
}
