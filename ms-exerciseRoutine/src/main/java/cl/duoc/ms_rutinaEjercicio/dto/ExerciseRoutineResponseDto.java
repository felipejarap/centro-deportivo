package cl.duoc.ms_rutinaEjercicio.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Modelo de respuesta que representa los datos públicos de una rutina de ejercicios")
public class ExerciseRoutineResponseDto {
    @Schema(description = "Identificador único de la rutina registrado en la base de datos", example = "1")
    private Long idRoutine;
    @Schema(description = "Identificador único del usuario/alumno", example = "45")
    private Long idUser;
    @Schema(description = "Identificador único del entrenador que asignó la rutina", example = "12")
    private Long idCoach;
    @Schema(description = "Nombre descriptivo de la rutina de entrenamiento", example = "Rutina Hipertrofia Tren Superior")
    private String name;
    @Schema(description = "Detalle paso a paso de los ejercicios, series y repeticiones", example = "4x12 Press Banca, 4x10 Dominadas, 3x15 Vuelos Laterales")
    private String description;
    @Schema(description = "Meta deportiva que busca alcanzar la rutina", example = "Aumento de masa muscular y fuerza")
    private String objective;
    @Schema(description = "Peso corporal registrado del alumno al iniciar el plan (en kg)", example = "78.5")
    private Double recordedWeight;
    @Schema(description = "Récord o marca personal actual del alumno en su ejercicio principal (en kg)", example = "100.0")
    private Double personalBrand;
    @Schema(description = "Fecha en la que se le entregó la rutina al alumno", example = "2026-06-19")
    private LocalDateTime assignmentDate;
    @Schema(description = "Estado de vigencia de la rutina (true = En uso / false = Archivada)", example = "true")
    private Boolean active;

    @Schema(description = "Información pública complementaria del alumno (Proveniente de MS_Usuarios)")
    private UserResponseDto users;
    @Schema(description = "Información pública complementaria del entrenador (Proveniente de MS_Entrenadores)")
    private CoachResponseDto coaches;
}
