package cl.duoc.ms_rutinaEjercicio.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class EntrenadorResponseDto {

    private Long id;
    private String nombre;
    private String appaterno;
    private String apmaterno;
    private String especialidad;
    private String email;
}
