package cl.duoc.ms_rutinaEjercicio.service;

import cl.duoc.ms_rutinaEjercicio.dto.ExerciseRoutineRequestDto;
import cl.duoc.ms_rutinaEjercicio.dto.ExerciseRoutineResponseDto;

import java.util.List;

public interface ExerciseRoutineService {
    List<ExerciseRoutineResponseDto> findAll();
    ExerciseRoutineResponseDto findById(Long id);
    ExerciseRoutineResponseDto create(ExerciseRoutineRequestDto routine);
    ExerciseRoutineResponseDto update(Long id, ExerciseRoutineRequestDto routine);
    boolean delete(Long id);
    List<ExerciseRoutineResponseDto> findByUserId(Long userId) throws Exception;
    List<ExerciseRoutineResponseDto> findByCoachId(Long coachId) throws Exception;
    List<ExerciseRoutineResponseDto> findByObjective(String objective);
    List<ExerciseRoutineResponseDto> findActivesByUserId(Long userId) throws Exception;
}
