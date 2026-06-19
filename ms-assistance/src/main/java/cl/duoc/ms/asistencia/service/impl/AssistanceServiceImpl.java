package cl.duoc.ms.asistencia.service.impl;

import cl.duoc.ms.asistencia.dto.AssistanceRequestDto;
import cl.duoc.ms.asistencia.dto.AssistanceResponseDto;
import cl.duoc.ms.asistencia.dto.ClasseResponseDto;
import cl.duoc.ms.asistencia.dto.UserResponseDto;
import cl.duoc.ms.asistencia.model.Assistance;
import cl.duoc.ms.asistencia.repository.AssistanceRepository;
import cl.duoc.ms.asistencia.service.AssistanceService;
import cl.duoc.ms.asistencia.service.api.ClasseClient;
import cl.duoc.ms.asistencia.service.api.UserClient;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AssistanceServiceImpl implements AssistanceService {

    private static final Logger log = LoggerFactory.getLogger(AssistanceServiceImpl.class);

    private final AssistanceRepository repository;
    private final UserClient userClient;
    private final ClasseClient classeClient;

    private Assistance toEntity(AssistanceRequestDto dto) {
        Assistance a = new Assistance();
        a.setIdUser(dto.getIdUser());
        a.setIdClasse(dto.getIdClasse());
        a.setArrivalTime(dto.getArrivalTime());
        a.setAssist(dto.getAssist());
        return a;
    }

    private AssistanceResponseDto toDto(Assistance entity) {
        AssistanceResponseDto dto = new AssistanceResponseDto();
        dto.setIdAssistance(entity.getIdAssistance());
        dto.setIdUser(entity.getIdUser());
        dto.setIdClasse(entity.getIdClasse());
        dto.setArrivalTime(entity.getArrivalTime());
        dto.setAssist(entity.getAssist());

        try {
            UserResponseDto user = userClient.findById(entity.getIdUser());
            dto.setUser(user);
            log.debug("Usuario enriquecido en respuesta: idUser={}", entity.getIdUser());
        } catch (Exception e) {
            log.warn("No se pudo obtener datos del usuario para enriquecer respuesta: idUser={}, motivo={}",
                    entity.getIdUser(), e.getMessage());
            dto.setUser(null);
        }

        try {
            ClasseResponseDto classe = classeClient.findById(entity.getIdClasse());
            dto.setClasse(classe);
            log.debug("Clase enriquecida en respuesta: idClasse={}", entity.getIdClasse());
        } catch (Exception e) {
            log.warn("No se pudo obtener datos de la clase para enriquecer respuesta: idClasse={}, motivo={}",
                    entity.getIdClasse(), e.getMessage());
            dto.setClasse(null);
        }

        return dto;
    }

    @Override
    public List<AssistanceResponseDto> findAll() {
        log.info("Consultando todas las asistencias");
        List<AssistanceResponseDto> result = repository.findAll().stream().map(this::toDto).toList();
        log.info("Total de asistencias encontradas: {}", result.size());
        return result;
    }

    @Override
    public AssistanceResponseDto findById(Long id) {
        log.info("Buscando asistencia por id: {}", id);
        return repository.findById(id)
                .map(entity -> {
                    log.info("Asistencia encontrada: idAssistance={}", id);
                    return toDto(entity);
                })
                .orElseGet(() -> {
                    log.warn("Asistencia no encontrada: idAssistance={}", id);
                    return null;
                });
    }

    @Override
    public AssistanceResponseDto create(AssistanceRequestDto assistance) {
        log.info("Creando asistencia: idUsuario={}, idClasse={}, horaLlegada={}, asistio={}",
                assistance.getIdUser(), assistance.getIdClasse(),
                assistance.getArrivalTime(), assistance.getAssist());

        log.debug("Validando existencia del usuario: idUser={}", assistance.getIdUser());
        UserResponseDto userFind = userClient.findById(assistance.getIdUser());
        if (userFind == null || userFind.getId() == null) {
            log.warn("Validación fallida: usuario no encontrado en ms-users: idUser={}", assistance.getIdUser());
            throw new IllegalArgumentException("El usuario con ID " + assistance.getIdUser() + " no existe.");
        }
        log.debug("Usuario validado correctamente: idUser={}, username={}", userFind.getId(), userFind.getUsername());

        log.debug("Validando existencia de la clase: idClasse={}", assistance.getIdClasse());
        ClasseResponseDto classeFind = classeClient.findById(assistance.getIdClasse());
        if (classeFind == null || classeFind.getId() == null) {
            log.warn("Validación fallida: clase no encontrada en ms-classes: idClasse={}", assistance.getIdClasse());
            throw new IllegalArgumentException("La clase con ID " + assistance.getIdClasse() + " no existe.");
        }
        log.debug("Clase validada correctamente: idClasse={}", classeFind.getId());

        AssistanceResponseDto saved = toDto(repository.save(toEntity(assistance)));
        log.info("Asistencia creada exitosamente: idAssistance={}, idUser={}, idClasse={}",
                saved.getIdAssistance(), saved.getIdUser(), saved.getIdClasse());
        return saved;
    }

    @Override
    public AssistanceResponseDto update(Long id, AssistanceRequestDto assistance) {
        log.info("Actualizando asistencia: idAssistance={}, idUser={}, idClasse={}",
                id, assistance.getIdUser(), assistance.getIdClasse());

        log.debug("Validando existencia del usuario: idUser={}", assistance.getIdUser());
        UserResponseDto userFind = userClient.findById(assistance.getIdUser());
        if (userFind == null || userFind.getId() == null) {
            log.warn("Validación fallida en actualización: usuario no encontrado: idUser={}", assistance.getIdUser());
            throw new IllegalArgumentException("El usuario con ID " + assistance.getIdUser() + " no existe.");
        }

        log.debug("Validando existencia de la clase: idClasse={}", assistance.getIdClasse());
        ClasseResponseDto classeFind = classeClient.findById(assistance.getIdClasse());
        if (classeFind == null || classeFind.getId() == null) {
            log.warn("Validación fallida en actualización: clase no encontrada: idClasse={}", assistance.getIdClasse());
            throw new IllegalArgumentException("La clase con ID " + assistance.getIdClasse() + " no existe.");
        }

        if (repository.existsById(id)) {
            Assistance entity = toEntity(assistance);
            entity.setIdAssistance(id);
            AssistanceResponseDto updated = toDto(repository.save(entity));
            log.info("Asistencia actualizada exitosamente: idAsistencia={}", id);
            return updated;
        }

        log.warn("No se pudo actualizar: asistencia no encontrada: idAsistencia={}", id);
        return null;
    }

    @Override
    public boolean delete(Long id) {
        log.info("Eliminando asistencia: idAssistance={}", id);
        if (repository.existsById(id)) {
            repository.deleteById(id);
            log.info("Asistencia eliminada exitosamente: idAssistance={}", id);
            return true;
        }
        log.warn("No se pudo eliminar: asistencia no encontrada: idAssistance={}", id);
        return false;
    }

    @Override
    public List<AssistanceResponseDto> findByUserId(Long userId) throws Exception {
        log.info("Buscando asistencias por usuario: idUser={}", userId);
        try {
            UserResponseDto userFind = userClient.findById(userId);
            if (userFind == null) {
                log.warn("Usuario no encontrado en ms-users: idUser={}", userId);
                return null;
            }
            List<AssistanceResponseDto> result = repository.findByIdUser(userId)
                    .stream().map(this::toDto).toList();
            log.info("Asistencias encontradas para usuario: idUser={}, total={}", userId, result.size());
            return result;
        } catch (Exception e) {
            log.error("Error al buscar asistencias por usuario: idUser={}, motivo={}", userId, e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public List<AssistanceResponseDto> findByClasseId(Long classeId) throws Exception {
        log.info("Buscando asistencias por clase: idClasse={}", classeId);
        try {
            ClasseResponseDto classeFind = classeClient.findById(classeId);
            if (classeFind == null) {
                log.warn("Clase no encontrada en ms-classes: idClasse={}", classeId);
                return null;
            }
            List<AssistanceResponseDto> result = repository.findByIdClasse(classeId)
                    .stream().map(this::toDto).toList();
            log.info("Asistencias encontradas para clase: idClasse={}, total={}", classeId, result.size());
            return result;
        } catch (Exception e) {
            log.error("Error al buscar asistencias por clase: idClasse={}, motivo={}", classeId, e.getMessage());
            throw new Exception(e.getMessage());
        }
    }
}
