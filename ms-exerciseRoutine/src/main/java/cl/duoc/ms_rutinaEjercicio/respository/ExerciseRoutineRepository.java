package cl.duoc.ms_rutinaEjercicio.respository;

import cl.duoc.ms_rutinaEjercicio.model.ExerciseRoutine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExerciseRoutineRepository extends JpaRepository <ExerciseRoutine, Long> {

    List<ExerciseRoutine> findByIdUser(Long idUser);
    List<ExerciseRoutine> findByIdCoach(Long idCoach);
    List<ExerciseRoutine> findByObjective(String objective);
    List<ExerciseRoutine> findByIdUserAndActive(Long idUser, Boolean active);
}
