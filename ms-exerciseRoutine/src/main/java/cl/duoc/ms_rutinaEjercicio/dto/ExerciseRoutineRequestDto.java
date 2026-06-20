package cl.duoc.ms_rutinaEjercicio.dto;

import jakarta.persistence.Column;
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
public class ExerciseRoutineRequestDto {
    @NotNull(message = "El id de usuario es requerido")
    private Long idUser;

    @NotNull(message = "El id de entrenador es requerido")
    private Long idCoach;

    @NotBlank(message = "El nombre de la rutina no puede estar vacío")
    private String name;

    private String description;

    @NotBlank(message = "El objetivo no puede estar vacío")
    private String objective;

    private Double recordedWeight;

    private Double personalBrand;

    @NotBlank(message = "La fecha de asignación no puede estar vacía")
    private String assignmentDate;

    @NotNull(message = "El campo activa es requerido")
    private Boolean active;



}
