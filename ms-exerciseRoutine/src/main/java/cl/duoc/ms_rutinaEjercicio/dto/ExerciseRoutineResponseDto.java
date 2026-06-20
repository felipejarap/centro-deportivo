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
public class ExerciseRoutineResponseDto {

    private Long idRoutine;
    private Long idUser;
    private Long idCoach;
    private String name;
    private String description;
    private String objective;
    private Double recordedWeight;
    private Double personalBrand;
    private String assignmentDate;
    private Boolean active;

    private UserResponseDto users;
    private CoachResponseDto coaches;
}
