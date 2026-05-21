package cl.duoc.ms_rutinaEjercicio.service.impl;

import cl.duoc.ms_rutinaEjercicio.dto.EntrenadorResponseDto;
import cl.duoc.ms_rutinaEjercicio.dto.RutinaEjercicioRequestDto;
import cl.duoc.ms_rutinaEjercicio.dto.RutinaEjercicioResponseDto;
import cl.duoc.ms_rutinaEjercicio.dto.UserResponseDto;
import cl.duoc.ms_rutinaEjercicio.model.RutinaEjercicio;
import cl.duoc.ms_rutinaEjercicio.respository.RutinaEjercicioRepository;
import cl.duoc.ms_rutinaEjercicio.service.RutinaEjercicioService;
import cl.duoc.ms_rutinaEjercicio.service.api.EntrenadorClient;
import cl.duoc.ms_rutinaEjercicio.service.api.UsuarioClient;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RutinaEjercicioServiceImpl implements RutinaEjercicioService {

    private static final Logger log = LoggerFactory.getLogger(RutinaEjercicioServiceImpl.class);

    private final RutinaEjercicioRepository repository;
    private final UsuarioClient usuarioClient;
    private final EntrenadorClient entrenadorClient;

    private RutinaEjercicio toEntity(RutinaEjercicioRequestDto dto) {
        RutinaEjercicio r = new RutinaEjercicio();
        r.setIdUsuario(dto.getIdUsuario());
        r.setIdEntrenador(dto.getIdEntrenador());
        r.setNombre(dto.getNombre());
        r.setDescripcion(dto.getDescripcion());
        r.setObjetivo(dto.getObjetivo());
        r.setPesoRegistrado(dto.getPesoRegistrado());
        r.setMarcaPersonal(dto.getMarcaPersonal());
        r.setFechaAsignacion(dto.getFechaAsignacion());
        r.setActiva(dto.getActiva());
        return r;
    }

    private RutinaEjercicioResponseDto toDto(RutinaEjercicio entity) {
        RutinaEjercicioResponseDto dto = new RutinaEjercicioResponseDto();
        dto.setIdRutina(entity.getIdRutina());
        dto.setIdUsuario(entity.getIdUsuario());
        dto.setIdEntrenador(entity.getIdEntrenador());
        dto.setNombre(entity.getNombre());
        dto.setDescripcion(entity.getDescripcion());
        dto.setObjetivo(entity.getObjetivo());
        dto.setPesoRegistrado(entity.getPesoRegistrado());
        dto.setMarcaPersonal(entity.getMarcaPersonal());
        dto.setFechaAsignacion(entity.getFechaAsignacion());
        dto.setActiva(entity.getActiva());

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
            EntrenadorResponseDto entrenador = entrenadorClient.findById(entity.getIdEntrenador());
            dto.setEntrenador(entrenador);
            log.debug("Entrenador enriquecido en respuesta: idEntrenador={}", entity.getIdEntrenador());
        } catch (Exception e) {
            log.warn("No se pudo obtener datos del entrenador para enriquecer respuesta: idEntrenador={}, motivo={}",
                    entity.getIdEntrenador(), e.getMessage());
            dto.setEntrenador(null);
        }

        return dto;
    }

    @Override
    public List<RutinaEjercicioResponseDto> findAll() {
        log.info("Consultando todas las rutinas de ejercicio");
        List<RutinaEjercicioResponseDto> result = repository.findAll().stream().map(this::toDto).toList();
        log.info("Total de rutinas encontradas: {}", result.size());
        return result;
    }

    @Override
    public RutinaEjercicioResponseDto findById(Long id) {
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
    public RutinaEjercicioResponseDto create(RutinaEjercicioRequestDto rutina) {
        log.info("Creando rutina: idUsuario={}, idEntrenador={}, nombre={}, objetivo={}",
                rutina.getIdUsuario(), rutina.getIdEntrenador(),
                rutina.getNombre(), rutina.getObjetivo());

        log.debug("Validando existencia del usuario: idUsuario={}", rutina.getIdUsuario());
        UserResponseDto userFind = usuarioClient.findById(rutina.getIdUsuario());
        if (userFind == null || userFind.getId() == null) {
            log.warn("Validación fallida: usuario no encontrado en ms-usuarios: idUsuario={}", rutina.getIdUsuario());
            throw new IllegalArgumentException("El usuario con ID " + rutina.getIdUsuario() + " no existe ");
        }
        log.debug("Usuario validado correctamente: idUsuario={}", userFind.getId());

        log.debug("Validando existencia del entrenador: idEntrenador={}", rutina.getIdEntrenador());
        EntrenadorResponseDto entrenadorFind = entrenadorClient.findById(rutina.getIdEntrenador());
        if (entrenadorFind == null || entrenadorFind.getId() == null) {
            log.warn("Validación fallida: entrenador no encontrado en ms-entrenadores: idEntrenador={}", rutina.getIdEntrenador());
            throw new IllegalArgumentException("El Entrenador con ID " + rutina.getIdEntrenador() + " no existe ");
        }
        log.debug("Entrenador validado correctamente: idEntrenador={}", entrenadorFind.getId());

        RutinaEjercicioResponseDto saved = toDto(repository.save(toEntity(rutina)));
        log.info("Rutina creada exitosamente: idRutina={}, idUsuario={}, idEntrenador={}",
                saved.getIdRutina(), saved.getIdUsuario(), saved.getIdEntrenador());
        return saved;
    }

    @Override
    public RutinaEjercicioResponseDto update(Long id, RutinaEjercicioRequestDto rutina) {
        log.info("Actualizando rutina: idRutina={}, idUsuario={}, idEntrenador={}",
                id, rutina.getIdUsuario(), rutina.getIdEntrenador());

        log.debug("Validando existencia del usuario: idUsuario={}", rutina.getIdUsuario());
        UserResponseDto userFind = usuarioClient.findById(rutina.getIdUsuario());
        if (userFind == null || userFind.getId() == null) {
            log.warn("Validación fallida en actualización: usuario no encontrado: idUsuario={}", rutina.getIdUsuario());
            throw new IllegalArgumentException("El usuario con ID " + rutina.getIdUsuario() + " no existe ");
        }

        log.debug("Validando existencia del entrenador: idEntrenador={}", rutina.getIdEntrenador());
        EntrenadorResponseDto entrenadorFind = entrenadorClient.findById(rutina.getIdEntrenador());
        if (entrenadorFind == null || entrenadorFind.getId() == null) {
            log.warn("Validación fallida en actualización: entrenador no encontrado: idEntrenador={}", rutina.getIdEntrenador());
            throw new IllegalArgumentException("El Entrenador con ID " + rutina.getIdEntrenador() + " no existe ");
        }

        if (repository.existsById(id)) {
            RutinaEjercicio entity = toEntity(rutina);
            entity.setIdRutina(id);
            RutinaEjercicioResponseDto updated = toDto(repository.save(entity));
            log.info("Rutina actualizada exitosamente: idRutina={}", id);
            return updated;
        }

        log.warn("No se pudo actualizar: rutina no encontrada: idRutina={}", id);
        return null;
    }

    @Override
    public boolean delete(Long id) {
        log.info("Eliminando rutina: idRutina={}", id);
        if (repository.existsById(id)) {
            repository.deleteById(id);
            log.info("Rutina eliminada exitosamente: idRutina={}", id);
            return true;
        }
        log.warn("No se pudo eliminar: rutina no encontrada: idRutina={}", id);
        return false;
    }

    @Override
    public List<RutinaEjercicioResponseDto> findByUserId(Long userId) throws Exception {
        log.info("Buscando rutinas por usuario: idUsuario={}", userId);
        try {
            UserResponseDto userFind = usuarioClient.findById(userId);
            if (userFind == null) {
                log.warn("Usuario no encontrado en ms-usuarios: idUsuario={}", userId);
                return null;
            }
            List<RutinaEjercicioResponseDto> result = repository.findByIdUsuario(userId)
                    .stream().map(this::toDto).toList();
            log.info("Rutinas encontradas para usuario: idUsuario={}, total={}", userId, result.size());
            return result;
        } catch (Exception e) {
            log.error("Error al buscar rutinas por usuario: idUsuario={}, motivo={}", userId, e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public List<RutinaEjercicioResponseDto> findByEntrenadorId(Long entrenadorId) throws Exception {
        log.info("Buscando rutinas por entrenador: idEntrenador={}", entrenadorId);
        try {
            EntrenadorResponseDto entrenadorFind = entrenadorClient.findById(entrenadorId);
            if (entrenadorFind == null) {
                log.warn("Entrenador no encontrado en ms-entrenadores: idEntrenador={}", entrenadorId);
                return null;
            }
            List<RutinaEjercicioResponseDto> result = repository.findByIdEntrenador(entrenadorId)
                    .stream().map(this::toDto).toList();
            log.info("Rutinas encontradas para entrenador: idEntrenador={}, total={}", entrenadorId, result.size());
            return result;
        } catch (Exception e) {
            log.error("Error al buscar rutinas por entrenador: idEntrenador={}, motivo={}", entrenadorId, e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public List<RutinaEjercicioResponseDto> findByObjetivo(String objetivo) {
        log.info("Buscando rutinas por objetivo: objetivo={}", objetivo);
        List<RutinaEjercicioResponseDto> result = repository.findByObjetivo(objetivo)
                .stream().map(this::toDto).toList();
        log.info("Rutinas encontradas para objetivo: objetivo={}, total={}", objetivo, result.size());
        return result;
    }

    @Override
    public List<RutinaEjercicioResponseDto> findActivasByUserId(Long userId) throws Exception {
        log.info("Buscando rutinas activas por usuario: idUsuario={}", userId);
        try {
            UserResponseDto userFind = usuarioClient.findById(userId);
            if (userFind == null) {
                log.warn("Usuario no encontrado en ms-usuarios al buscar rutinas activas: idUsuario={}", userId);
                return null;
            }
            List<RutinaEjercicioResponseDto> result = repository.findByIdUsuarioAndActiva(userId, true)
                    .stream().map(this::toDto).toList();
            log.info("Rutinas activas encontradas para usuario: idUsuario={}, total={}", userId, result.size());
            return result;
        } catch (Exception e) {
            log.error("Error al buscar rutinas activas por usuario: idUsuario={}, motivo={}", userId, e.getMessage());
            throw new Exception(e.getMessage());
        }
    }
}