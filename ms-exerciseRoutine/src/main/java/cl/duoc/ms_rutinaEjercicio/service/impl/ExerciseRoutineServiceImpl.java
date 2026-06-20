package cl.duoc.ms_rutinaEjercicio.service.impl;

import cl.duoc.ms_rutinaEjercicio.dto.CoachResponseDto;
import cl.duoc.ms_rutinaEjercicio.dto.ExerciseRoutineRequestDto;
import cl.duoc.ms_rutinaEjercicio.dto.ExerciseRoutineResponseDto;
import cl.duoc.ms_rutinaEjercicio.dto.UserResponseDto;
import cl.duoc.ms_rutinaEjercicio.model.ExerciseRoutine;
import cl.duoc.ms_rutinaEjercicio.respository.ExerciseRoutineRepository;
import cl.duoc.ms_rutinaEjercicio.service.ExerciseRoutineService;
import cl.duoc.ms_rutinaEjercicio.service.api.CoachClient;
import cl.duoc.ms_rutinaEjercicio.service.api.UserClient;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExerciseRoutineServiceImpl implements ExerciseRoutineService {

    private static final Logger log = LoggerFactory.getLogger(ExerciseRoutineServiceImpl.class);

    private final ExerciseRoutineRepository repository;
    private final UserClient userClient;
    private final CoachClient coachClient;

    private ExerciseRoutine toEntity(ExerciseRoutineRequestDto dto) {
        ExerciseRoutine r = new ExerciseRoutine();
        r.setIdUser(dto.getIdUser());
        r.setIdCoach(dto.getIdCoach());
        r.setName(dto.getName());
        r.setDescription(dto.getDescription());
        r.setObjective(dto.getObjective());
        r.setRecordedWeight(dto.getRecordedWeight());
        r.setPersonalBrand(dto.getPersonalBrand());
        r.setAssignmentDate(dto.getAssignmentDate());
        r.setActive(dto.getActive());
        return r;
    }

    private ExerciseRoutineResponseDto toDto(ExerciseRoutine entity) {
        ExerciseRoutineResponseDto dto = new ExerciseRoutineResponseDto();
        dto.setIdRoutine(entity.getIdRoutine());
        dto.setIdUser(entity.getIdUser());
        dto.setIdCoach(entity.getIdCoach());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setObjective(entity.getObjective());
        dto.setRecordedWeight(entity.getRecordedWeight());
        dto.setPersonalBrand(entity.getPersonalBrand());
        dto.setAssignmentDate(entity.getAssignmentDate());
        dto.setActive(entity.getActive());

        try {
            UserResponseDto user = userClient.findById(entity.getIdUser());
            dto.setUsers(user);
            log.debug("Usuario enriquecido en respuesta: idUser={}", entity.getIdUser());
        } catch (Exception e) {
            log.warn("No se pudo obtener datos del usuario para enriquecer respuesta: idUsuario={}, motivo={}",
                    entity.getIdUser(), e.getMessage());
            dto.setUsers(null);
        }

        try {
            CoachResponseDto coach = coachClient.findById(entity.getIdCoach());
            dto.setCoaches(coach);
            log.debug("Entrenador enriquecido en respuesta: idCoach={}", entity.getIdCoach());
        } catch (Exception e) {
            log.warn("No se pudo obtener datos del entrenador para enriquecer respuesta: idCoach={}, motivo={}",
                    entity.getIdCoach(), e.getMessage());
            dto.setCoaches(null);
        }

        return dto;
    }

    @Override
    public List<ExerciseRoutineResponseDto> findAll() {
        log.info("Consultando todas las rutinas de ejercicio");
        List<ExerciseRoutineResponseDto> result = repository.findAll().stream().map(this::toDto).toList();
        log.info("Total de rutinas encontradas: {}", result.size());
        return result;
    }

    @Override
    public ExerciseRoutineResponseDto findById(Long id) {
        log.info("Buscando rutina por id: {}", id);
        return repository.findById(id)
                .map(entity -> {
                    log.info("Rutina encontrada: idRutina={}", id);
                    return toDto(entity);
                })
                .orElseGet(() -> {
                    log.warn("Rutina no encontrada: idRutina={}", id);
                    return null;
                });
    }

    @Override
    public ExerciseRoutineResponseDto create(ExerciseRoutineRequestDto routine) {
        log.info("Creando rutina: idUser={}, idCoach={}, name={}, objetive={}",
                routine.getIdUser(), routine.getIdCoach(),
                routine.getName(), routine.getObjective());

        log.debug("Validando existencia del usuario: idUser={}", routine.getIdUser());
        UserResponseDto userFind = userClient.findById(routine.getIdUser());
        if (userFind == null || userFind.getId() == null) {
            log.warn("Validación fallida: usuario no encontrado en ms-usuarios: idUser={}", routine.getIdUser());
            throw new IllegalArgumentException("El usuario con ID " + routine.getIdUser() + " no existe ");
        }
        log.debug("Usuario validado correctamente: idUser={}", userFind.getId());

        log.debug("Validando existencia del entrenador: idCoach={}", routine.getIdCoach());
        CoachResponseDto coachFind = coachClient.findById(routine.getIdCoach());
        if (coachFind == null || coachFind.getId() == null) {
            log.warn("Validación fallida: entrenador no encontrado en ms-coaches: idCoach={}", routine.getIdCoach());
            throw new IllegalArgumentException("El Entrenador con ID " + routine.getIdCoach() + " no existe ");
        }
        log.debug("Entrenador validado correctamente: idCoach={}", coachFind.getId());

        ExerciseRoutineResponseDto saved = toDto(repository.save(toEntity(routine)));
        log.info("Rutina creada exitosamente: idRoutine={}, idUser={}, idCoach={}",
                saved.getIdRoutine(), saved.getIdUser(), saved.getIdCoach());
        return saved;
    }

    @Override
    public ExerciseRoutineResponseDto update(Long id, ExerciseRoutineRequestDto routine) {
        log.info("Actualizando rutina: idRoutine={}, idUser={}, idCoach={}",
                id, routine.getIdUser(), routine.getIdCoach());

        log.debug("Validando existencia del usuario: idUser={}", routine.getIdUser());
        UserResponseDto userFind = userClient.findById(routine.getIdUser());
        if (userFind == null || userFind.getId() == null) {
            log.warn("Validación fallida en actualización: usuario no encontrado: idUser={}", routine.getIdUser());
            throw new IllegalArgumentException("El usuario con ID " + routine.getIdUser() + " no existe ");
        }

        log.debug("Validando existencia del entrenador: idCoach={}", routine.getIdCoach());
        CoachResponseDto entrenadorFind = coachClient.findById(routine.getIdCoach());
        if (entrenadorFind == null || entrenadorFind.getId() == null) {
            log.warn("Validación fallida en actualización: entrenador no encontrado: idEntrenador={}", routine.getIdCoach());
            throw new IllegalArgumentException("El Entrenador con ID " + routine.getIdCoach() + " no existe ");
        }

        if (repository.existsById(id)) {
            ExerciseRoutine entity = toEntity(routine);
            entity.setIdRoutine(id);
            ExerciseRoutineResponseDto updated = toDto(repository.save(entity));
            log.info("Rutina actualizada exitosamente: idRoutine={}", id);
            return updated;
        }

        log.warn("No se pudo actualizar: rutina no encontrada: idRoutine={}", id);
        return null;
    }

    @Override
    public boolean delete(Long id) {
        log.info("Eliminando rutina: idRoutine={}", id);
        if (repository.existsById(id)) {
            repository.deleteById(id);
            log.info("Rutina eliminada exitosamente: idRoutine={}", id);
            return true;
        }
        log.warn("No se pudo eliminar: rutina no encontrada: idRoutine={}", id);
        return false;
    }

    @Override
    public List<ExerciseRoutineResponseDto> findByUserId(Long userId) throws Exception {
        log.info("Buscando rutinas por usuario: idUser={}", userId);
        try {
            UserResponseDto userFind = userClient.findById(userId);
            if (userFind == null) {
                log.warn("Usuario no encontrado en ms-usuarios: idUser={}", userId);
                return null;
            }
            List<ExerciseRoutineResponseDto> result = repository.findByIdUser(userId)
                    .stream().map(this::toDto).toList();
            log.info("Rutinas encontradas para usuario: idUser={}, total={}", userId, result.size());
            return result;
        } catch (Exception e) {
            log.error("Error al buscar rutinas por usuario: idUser={}, motivo={}", userId, e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public List<ExerciseRoutineResponseDto> findByCoachId(Long coachId) throws Exception {
        log.info("Buscando rutinas por entrenador: idCoach={}", coachId);
        try {
            CoachResponseDto coachFind = coachClient.findById(coachId);
            if (coachFind == null) {
                log.warn("Entrenador no encontrado en ms-entrenadores: idCoach={}", coachId);
                return null;
            }
            List<ExerciseRoutineResponseDto> result = repository.findByIdCoach(coachId)
                    .stream().map(this::toDto).toList();
            log.info("Rutinas encontradas para entrenador: idEntrenador={}, total={}", coachId, result.size());
            return result;
        } catch (Exception e) {
            log.error("Error al buscar rutinas por entrenador: idEntrenador={}, motivo={}", coachId, e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public List<ExerciseRoutineResponseDto> findByObjective(String objective) {
        log.info("Buscando rutinas por objetivo: objetive={}", objective);
        List<ExerciseRoutineResponseDto> result = repository.findByObjective(objective)
                .stream().map(this::toDto).toList();
        log.info("Rutinas encontradas para objetivo: objetive={}, total={}", objective, result.size());
        return result;
    }

    @Override
    public List<ExerciseRoutineResponseDto> findActivesByUserId(Long userId) throws Exception {
        log.info("Buscando rutinas activas por usuario: idUsuario={}", userId);
        try {
            UserResponseDto userFind = userClient.findById(userId);
            if (userFind == null) {
                log.warn("Usuario no encontrado en ms-usuarios al buscar rutinas activas: idUsuario={}", userId);
                return null;
            }
            List<ExerciseRoutineResponseDto> result = repository.findByIdUserAndActive(userId, true)
                    .stream().map(this::toDto).toList();
            log.info("Rutinas activas encontradas para usuario: idUser={}, total={}", userId, result.size());
            return result;
        } catch (Exception e) {
            log.error("Error al buscar rutinas activas por usuario: idUser={}, motivo={}", userId, e.getMessage());
            throw new Exception(e.getMessage());
        }
    }
}