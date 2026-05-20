package cl.duoc.ms_rutinaEjercicio.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RutinaEjercicioRequestDto {
    @NotNull(message = "El id de usuario es requerido")
    private Long idUsuario;

    @NotNull(message = "El id de entrenador es requerido")
    private Long idEntrenador;

    @NotBlank(message = "El nombre de la rutina no puede estar vacío")
    private String nombre;

    private String descripcion;

    @NotBlank(message = "El objetivo no puede estar vacío")
    private String objetivo;

    private Double pesoRegistrado;

    private Double marcaPersonal;

    @NotBlank(message = "La fecha de asignación no puede estar vacía")
    private String fechaAsignacion;

    @NotNull(message = "El campo activa es requerido")
    private Boolean activa;
}
