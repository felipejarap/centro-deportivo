package cl.duoc.ms_reservas.service.impl;

import cl.duoc.ms_reservas.dto.*;
import cl.duoc.ms_reservas.model.Reserva;
import cl.duoc.ms_reservas.repository.ReservaRespository;
import cl.duoc.ms_reservas.service.ReservaService;
import cl.duoc.ms_reservas.service.api.ClasseClient;
import cl.duoc.ms_reservas.service.api.EntrenadorClient;
import cl.duoc.ms_reservas.service.api.UsuarioClient;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservaServiceImpl implements ReservaService {

    private static final Logger log = LoggerFactory.getLogger(ReservaServiceImpl.class);

    private final ReservaRespository repository;
    private final UsuarioClient usuarioClient;
    private final EntrenadorClient entrenadorClient;
    private final ClasseClient claseClient;

    private Reserva toEntity(ReservaRequestDto dto) {
        Reserva r = new Reserva();
        r.setIdUsuario(dto.getIdUsuario());
        r.setIdClase(dto.getIdClase());
        r.setIdEntrenador(dto.getIdEntrenador());
        r.setFechaReserva(dto.getFechaReserva());
        r.setEstadoReserva(dto.getEstadoReserva());
        return r;
    }

    private ReservaResponseDto toDto(Reserva entity) {
        ReservaResponseDto dto = new ReservaResponseDto();
        dto.setIdReserva(entity.getIdReserva());
        dto.setIdUsuario(entity.getIdUsuario());
        dto.setIdClase(entity.getIdClase());
        dto.setIdEntrenador(entity.getIdEntrenador());
        dto.setFechaReserva(entity.getFechaReserva());
        dto.setEstadoReserva(entity.getEstadoReserva());

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

        try {
            ClasseResponseDto clase = claseClient.findById(entity.getIdClase());
            dto.setClase(clase);
            log.debug("Clase enriquecida en respuesta: idClase={}", entity.getIdClase());
        } catch (Exception e) {
            log.warn("No se pudo obtener datos de la clase para enriquecer respuesta: idClase={}, motivo={}",
                    entity.getIdClase(), e.getMessage());
            dto.setClase(null);
        }

        return dto;
    }

    @Override
    public List<ReservaResponseDto> findAll() {
        log.info("Consultando todas las reservas");
        List<ReservaResponseDto> result = repository.findAll().stream().map(this::toDto).toList();
        log.info("Total de reservas encontradas: {}", result.size());
        return result;
    }

    @Override
    public ReservaResponseDto findById(Long id) {
        log.info("Buscando reserva por id: {}", id);
        return repository.findById(id)
                .map(entity -> {
                    log.info("Reserva encontrada: idReserva={}", id);
                    return toDto(entity);
                })
                .orElseGet(() -> {
                    log.warn("Reserva no encontrada: idReserva={}", id);
                    return null;
                });
    }

    @Override
    public ReservaResponseDto create(ReservaRequestDto reserva) {
        log.info("Creando reserva: idUsuario={}, idClase={}, idEntrenador={}, fechaReserva={}, estadoReserva={}",
                reserva.getIdUsuario(), reserva.getIdClase(),
                reserva.getIdEntrenador(), reserva.getFechaReserva(), reserva.getEstadoReserva());
        ReservaResponseDto saved = toDto(repository.save(toEntity(reserva)));
        log.info("Reserva creada exitosamente: idReserva={}, idUsuario={}", saved.getIdReserva(), saved.getIdUsuario());
        return saved;
    }

    @Override
    public ReservaResponseDto update(Long id, ReservaRequestDto reserva) {
        log.info("Actualizando reserva: idReserva={}, idUsuario={}, idClase={}, idEntrenador={}",
                id, reserva.getIdUsuario(), reserva.getIdClase(), reserva.getIdEntrenador());
        if (repository.existsById(id)) {
            Reserva entity = toEntity(reserva);
            entity.setIdReserva(id);
            ReservaResponseDto updated = toDto(repository.save(entity));
            log.info("Reserva actualizada exitosamente: idReserva={}", id);
            return updated;
        }
        log.warn("No se pudo actualizar: reserva no encontrada: idReserva={}", id);
        return null;
    }

    @Override
    public boolean delete(Long id) {
        log.info("Eliminando reserva: idReserva={}", id);
        if (repository.existsById(id)) {
            repository.deleteById(id);
            log.info("Reserva eliminada exitosamente: idReserva={}", id);
            return true;
        }
        log.warn("No se pudo eliminar: reserva no encontrada: idReserva={}", id);
        return false;
    }

    @Override
    public List<ReservaResponseDto> findByUserId(Long userId) throws Exception {
        log.info("Buscando reservas por usuario: idUsuario={}", userId);
        try {
            UserResponseDto userFind = usuarioClient.findById(userId);
            if (userFind == null) {
                log.warn("Usuario no encontrado en ms-usuarios: idUsuario={}", userId);
                return null;
            }
            List<ReservaResponseDto> result = repository.findByUserId(userId)
                    .stream().map(this::toDto).toList();
            log.info("Reservas encontradas para usuario: idUsuario={}, total={}", userId, result.size());
            return result;
        } catch (Exception e) {
            log.error("Error al buscar reservas por usuario: idUsuario={}, motivo={}", userId, e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public List<ReservaResponseDto> findByEntrenadorId(Long entrenadorId) throws Exception {
        log.info("Buscando reservas por entrenador: idEntrenador={}", entrenadorId);
        try {
            EntrenadorResponseDto entrenadorFind = entrenadorClient.findById(entrenadorId);
            if (entrenadorFind == null) {
                log.warn("Entrenador no encontrado en ms-entrenadores: idEntrenador={}", entrenadorId);
                return null;
            }
            List<ReservaResponseDto> result = repository.findByEntrenadorId(entrenadorId)
                    .stream().map(this::toDto).toList();
            log.info("Reservas encontradas para entrenador: idEntrenador={}, total={}", entrenadorId, result.size());
            return result;
        } catch (Exception e) {
            log.error("Error al buscar reservas por entrenador: idEntrenador={}, motivo={}", entrenadorId, e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public List<ReservaResponseDto> findByClasseId(Long classeId) throws Exception {
        log.info("Buscando reservas por clase: idClase={}", classeId);
        try {
            ClasseResponseDto classeFind = claseClient.findById(classeId);
            if (classeFind == null) {
                log.warn("Clase no encontrada en ms-classes: idClase={}", classeId);
                return null;
            }
            List<ReservaResponseDto> result = repository.findByClasseId(classeId)
                    .stream().map(this::toDto).toList();
            log.info("Reservas encontradas para clase: idClase={}, total={}", classeId, result.size());
            return result;
        } catch (Exception e) {
            log.error("Error al buscar reservas por clase: idClase={}, motivo={}", classeId, e.getMessage());
            throw new Exception(e.getMessage());
        }
    }
}