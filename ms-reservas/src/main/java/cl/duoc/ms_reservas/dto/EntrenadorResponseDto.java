package cl.duoc.ms_reservas.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EntrenadorResponseDto {

    private Long idEntrenador;
    private String nombre;
    private String appaterno;
    private String apmaterno;
    private String especialidad;
    private String certificacion;
}
