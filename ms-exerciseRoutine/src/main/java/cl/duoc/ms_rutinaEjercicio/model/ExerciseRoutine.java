package cl.duoc.ms_rutinaEjercicio.model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ExerciseRoutine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_routine")
    private Long idRoutine;

    @Column(name = "id_user", nullable = false)
    private Long idUser;

    @Column(name = "id_coach", nullable = false)
    private Long idCoach;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "objective", nullable = false)
    private String objective; // hipertrofia, cardio, fuerza, flexibilidad, etc.

    @Column(name = "recorded_weight")
    private Double recordedWeight;

    @Column(name = "personal_brand")
    private Double personalBrand;

    @Column(name = "assignment_date", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime assignmentDate;

    @Column(name = "active", nullable = false)
    private Boolean active;
}
