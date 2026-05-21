package cl.duoc.ms.asistencia.service.impl;

import cl.duoc.ms.asistencia.dto.AsistenciaRequestDto;
import cl.duoc.ms.asistencia.dto.AsistenciaResponseDto;
import cl.duoc.ms.asistencia.dto.ClasseResponseDto;
import cl.duoc.ms.asistencia.dto.UserResponseDto;
import cl.duoc.ms.asistencia.model.Asistencia;
import cl.duoc.ms.asistencia.repository.AsistenciaRepository;
import cl.duoc.ms.asistencia.service.AsistenciaService;
import cl.duoc.ms.asistencia.service.api.ClasseClient;
import cl.duoc.ms.asistencia.service.api.UsuarioClient;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AsistenciaServiceImpl implements AsistenciaService {

    private static final Logger log = LoggerFactory.getLogger(AsistenciaServiceImpl.class);

    private final AsistenciaRepository repository;
    private final UsuarioClient usuarioClient;
    private final ClasseClient classeClient;

    private Asistencia toEntity(AsistenciaRequestDto dto) {
        Asistencia a = new Asistencia();
        a.setIdUsuario(dto.getIdUsuario());
        a.setIdClasse(dto.getIdClasse());
        a.setHoraLlegada(dto.getHoraLlegada());
        a.setAsistio(dto.getAsistio());
        return a;
    }

    private AsistenciaResponseDto toDto(Asistencia entity) {
        AsistenciaResponseDto dto = new AsistenciaResponseDto();
        dto.setIdAsistencia(entity.getIdAsistencia());
        dto.setIdUsuario(entity.getIdUsuario());
        dto.setIdClasse(entity.getIdClasse());
        dto.setHoraLlegada(entity.getHoraLlegada());
        dto.setAsistio(entity.getAsistio());

        try {
            UserResponseDto usuario = usuarioClient.findById(entity.getIdUsuario());
            dto.setUsuario(usuario);
            log.debug("Usuario enriquecido en respuesta: idUsuario={}", entity.getIdUsuario());
        } catch (Exception e) {
            log.warn("No se pudo obtener datos del usuario para enriquecer respuesta: idUsuario={}, motivo={}",
                    entity.getIdUsuario(), e.getMessage());
            dto.setUsuario(null);
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
    public List<AsistenciaResponseDto> findAll() {
        log.info("Consultando todas las asistencias");
        List<AsistenciaResponseDto> result = repository.findAll().stream().map(this::toDto).toList();
        log.info("Total de asistencias encontradas: {}", result.size());
        return result;
    }

    @Override
    public AsistenciaResponseDto findById(Long id) {
        log.info("Buscando asistencia por id: {}", id);
        return repository.findById(id)
                .map(entity -> {
                    log.info("Asistencia encontrada: idAsistencia={}", id);
                    return toDto(entity);
                })
                .orElseGet(() -> {
                    log.warn("Asistencia no encontrada: idAsistencia={}", id);
                    return null;
                });
    }

    @Override
    public AsistenciaResponseDto create(AsistenciaRequestDto asistencia) {
        log.info("Creando asistencia: idUsuario={}, idClasse={}, horaLlegada={}, asistio={}",
                asistencia.getIdUsuario(), asistencia.getIdClasse(),
                asistencia.getHoraLlegada(), asistencia.getAsistio());

        log.debug("Validando existencia del usuario: idUsuario={}", asistencia.getIdUsuario());
        UserResponseDto userFind = usuarioClient.findById(asistencia.getIdUsuario());
        if (userFind == null || userFind.getId() == null) {
            log.warn("Validación fallida: usuario no encontrado en ms-usuarios: idUsuario={}", asistencia.getIdUsuario());
            throw new IllegalArgumentException("El usuario con ID " + asistencia.getIdUsuario() + " no existe.");
        }
        log.debug("Usuario validado correctamente: idUsuario={}, username={}", userFind.getId(), userFind.getUsername());

        log.debug("Validando existencia de la clase: idClasse={}", asistencia.getIdClasse());
        ClasseResponseDto classeFind = classeClient.findById(asistencia.getIdClasse());
        if (classeFind == null || classeFind.getId() == null) {
            log.warn("Validación fallida: clase no encontrada en ms-classes: idClasse={}", asistencia.getIdClasse());
            throw new IllegalArgumentException("La clase con ID " + asistencia.getIdClasse() + " no existe.");
        }
        log.debug("Clase validada correctamente: idClasse={}", classeFind.getId());

        AsistenciaResponseDto saved = toDto(repository.save(toEntity(asistencia)));
        log.info("Asistencia creada exitosamente: idAsistencia={}, idUsuario={}, idClasse={}",
                saved.getIdAsistencia(), saved.getIdUsuario(), saved.getIdClasse());
        return saved;
    }

    @Override
    public AsistenciaResponseDto update(Long id, AsistenciaRequestDto asistencia) {
        log.info("Actualizando asistencia: idAsistencia={}, idUsuario={}, idClasse={}",
                id, asistencia.getIdUsuario(), asistencia.getIdClasse());

        log.debug("Validando existencia del usuario: idUsuario={}", asistencia.getIdUsuario());
        UserResponseDto userFind = usuarioClient.findById(asistencia.getIdUsuario());
        if (userFind == null || userFind.getId() == null) {
            log.warn("Validación fallida en actualización: usuario no encontrado: idUsuario={}", asistencia.getIdUsuario());
            throw new IllegalArgumentException("El usuario con ID " + asistencia.getIdUsuario() + " no existe.");
        }

        log.debug("Validando existencia de la clase: idClasse={}", asistencia.getIdClasse());
        ClasseResponseDto classeFind = classeClient.findById(asistencia.getIdClasse());
        if (classeFind == null || classeFind.getId() == null) {
            log.warn("Validación fallida en actualización: clase no encontrada: idClasse={}", asistencia.getIdClasse());
            throw new IllegalArgumentException("La clase con ID " + asistencia.getIdClasse() + " no existe.");
        }

        if (repository.existsById(id)) {
            Asistencia entity = toEntity(asistencia);
            entity.setIdAsistencia(id);
            AsistenciaResponseDto updated = toDto(repository.save(entity));
            log.info("Asistencia actualizada exitosamente: idAsistencia={}", id);
            return updated;
        }

        log.warn("No se pudo actualizar: asistencia no encontrada: idAsistencia={}", id);
        return null;
    }

    @Override
    public boolean delete(Long id) {
        log.info("Eliminando asistencia: idAsistencia={}", id);
        if (repository.existsById(id)) {
            repository.deleteById(id);
            log.info("Asistencia eliminada exitosamente: idAsistencia={}", id);
            return true;
        }
        log.warn("No se pudo eliminar: asistencia no encontrada: idAsistencia={}", id);
        return false;
    }

    @Override
    public List<AsistenciaResponseDto> findByUserId(Long userId) throws Exception {
        log.info("Buscando asistencias por usuario: idUsuario={}", userId);
        try {
            UserResponseDto userFind = usuarioClient.findById(userId);
            if (userFind == null) {
                log.warn("Usuario no encontrado en ms-usuarios: idUsuario={}", userId);
                return null;
            }
            List<AsistenciaResponseDto> result = repository.findByIdUsuario(userId)
                    .stream().map(this::toDto).toList();
            log.info("Asistencias encontradas para usuario: idUsuario={}, total={}", userId, result.size());
            return result;
        } catch (Exception e) {
            log.error("Error al buscar asistencias por usuario: idUsuario={}, motivo={}", userId, e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public List<AsistenciaResponseDto> findByClasseId(Long classeId) throws Exception {
        log.info("Buscando asistencias por clase: idClasse={}", classeId);
        try {
            ClasseResponseDto classeFind = classeClient.findById(classeId);
            if (classeFind == null) {
                log.warn("Clase no encontrada en ms-classes: idClasse={}", classeId);
                return null;
            }
            List<AsistenciaResponseDto> result = repository.findByIdClasse(classeId)
                    .stream().map(this::toDto).toList();
            log.info("Asistencias encontradas para clase: idClasse={}, total={}", classeId, result.size());
            return result;
        } catch (Exception e) {
            log.error("Error al buscar asistencias por clase: idClasse={}, motivo={}", classeId, e.getMessage());
            throw new Exception(e.getMessage());
        }
    }
}
