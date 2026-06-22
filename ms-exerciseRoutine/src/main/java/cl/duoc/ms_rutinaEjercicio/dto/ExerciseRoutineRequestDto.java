package cl.duoc.ms_rutinaEjercicio.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Modelo de entrada requerido para registrar o actualizar una rutina de ejercicios personalizada")

public class ExerciseRoutineRequestDto {
    @NotNull(message = "El id de usuario es requerido")
    @Schema(description = "Identificador único del usuario/alumno (Proveniente de MS_Usuarios)", example = "45")
    private Long idUser;

    @NotNull(message = "El id de entrenador es requerido")
    @Schema(description = "Identificador único del entrenador que asigna la rutina (Proveniente de MS_Entrenadores)", example = "12")
    private Long idCoach;

    @NotBlank(message = "El nombre de la rutina no puede estar vacío")
    @Schema(description = "Nombre comercial o descriptivo de la rutina de entrenamiento", example = "Rutina Hipertrofia Tren Superior")
    private String name;
    @Schema(description = "Detalle paso a paso de los ejercicios, series y repeticiones (Opcional)", example = "4x12 Press Banca, 4x10 Dominadas, 3x15 Vuelos Laterales")
    private String description;
    @NotBlank(message = "El objetivo no puede estar vacío")
    @Schema(description = "Meta deportiva que busca alcanzar la rutina", example = "Aumento de masa muscular y fuerza")
    private String objective;
    @Schema(description = "Peso corporal registrado del alumno al iniciar el plan (en kg)", example = "78.5")
    private Double recordedWeight;
    @Schema(description = "Récord o marca personal actual del alumno en su ejercicio principal (en kg)", example = "100.0")
    private Double personalBrand;
    @NotBlank(message = "La fecha de asignación no puede estar vacía")
    @Schema(description = "Fecha en la que se le entrega la rutina al alumno (Formato ISO o texto)", example = "2026-06-19")
    private LocalDateTime assignmentDate;
    @NotNull(message = "El campo activa es requerido")
    @Schema(description = "Estado de vigencia de la rutina (true = En uso / false = Archivada)", example = "true")
    private Boolean active;



}
