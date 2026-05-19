package cl.Duoc.MS_Entrenadores.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EntrenadorRequestDto {


    @NotBlank(message = "El nombre no puede estar en blanco")
    @Size(min = 4,max = 20, message = "El nombre debe tener entre 4 y 20 caracteres")
    private String nombre;

    @NotBlank(message = "El apellido no debe estar en blanco")
    private String appaterno;


    private String apmaterno;

    @NotBlank(message = "La especialidad no debe estar ne blanco")
    private String especialidad;

    @NotBlank(message = "La certificacion no debe estar en blanco")
    private String certificacion;


}
