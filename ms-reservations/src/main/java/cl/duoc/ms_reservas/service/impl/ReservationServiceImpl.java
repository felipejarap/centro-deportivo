package cl.duoc.ms_reservas.service.impl;

import cl.duoc.ms_reservas.dto.*;
import cl.duoc.ms_reservas.model.Reservation;
import cl.duoc.ms_reservas.repository.ReservationRespository;
import cl.duoc.ms_reservas.service.ReservationService;
import cl.duoc.ms_reservas.service.api.ClasseClient;
import cl.duoc.ms_reservas.service.api.CoachClient;
import cl.duoc.ms_reservas.service.api.UserClient;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private static final Logger log = LoggerFactory.getLogger(ReservationServiceImpl.class);

    private final ReservationRespository repository;
    private final UserClient userClient;
    private final CoachClient coachClient;
    private final ClasseClient claseClient;

    private Reservation toEntity(ReservationRequestDto dto) {
        Reservation r = new Reservation();
        r.setIdUser(dto.getIdUser());
        r.setIdClasse(dto.getIdClasse());
        r.setIdCoach(dto.getIdCoach());
        r.setReservationDate(dto.getReservationDate());
        r.setReservationStatus(dto.getReservationStatus());
        return r;
    }

    private ReservationResponseDto toDto(Reservation entity) {
        ReservationResponseDto dto = new ReservationResponseDto();
        dto.setIdReservation(entity.getIdReservation());
        dto.setIdUser(entity.getIdUser());
        dto.setIdClasse(entity.getIdClasse());
        dto.setIdCoach(entity.getIdCoach());
        dto.setReservationDate(entity.getReservationDate());
        dto.setReservationStatus(entity.getReservationStatus());

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
            CoachResponseDto coach = coachClient.findById(entity.getIdCoach());
            dto.setCoach(coach);
            log.debug("Entrenador enriquecido en respuesta: idCoach={}", entity.getIdCoach());
        } catch (Exception e) {
            log.warn("No se pudo obtener datos del entrenador para enriquecer respuesta: idCoach={}, motivo={}",
                    entity.getIdCoach(), e.getMessage());
            dto.setCoach(null);
        }

        try {
            ClasseResponseDto classe = claseClient.findById(entity.getIdClasse());
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
    public List<ReservationResponseDto> findAll() {
        log.info("Consultando todas las reservas");
        List<ReservationResponseDto> result = repository.findAll().stream().map(this::toDto).toList();
        log.info("Total de reservas encontradas: {}", result.size());
        return result;
    }

    @Override
    public ReservationResponseDto findById(Long id) {
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
    public ReservationResponseDto create(ReservationRequestDto reservation) {
        log.info("Creando reserva: idUsuario={}, idClase={}, idEntrenador={}, fechaReserva={}, estadoReserva={}",
                reservation.getIdUser(), reservation.getIdClasse(),
                reservation.getIdCoach(), reservation.getReservationDate(), reservation.getReservationStatus());
        ReservationResponseDto saved = toDto(repository.save(toEntity(reservation)));
        log.info("Reserva creada exitosamente: idReservation={}, idUser={}", saved.getIdReservation(), saved.getIdUser());
        return saved;
    }

    @Override
    public ReservationResponseDto update(Long id, ReservationRequestDto reservation) {
        log.info("Actualizando reserva: idReservation={}, idUsuario={}, idClase={}, idEntrenador={}",
                id, reservation.getIdUser(), reservation.getIdClasse(), reservation.getIdCoach());
        if (repository.existsById(id)) {
            Reservation entity = toEntity(reservation);
            entity.setIdReservation(id);
            ReservationResponseDto updated = toDto(repository.save(entity));
            log.info("Reserva actualizada exitosamente: idReservation={}", id);
            return updated;
        }
        log.warn("No se pudo actualizar: reserva no encontrada: idReservation={}", id);
        return null;
    }

    @Override
    public boolean delete(Long id) {
        log.info("Eliminando reserva: idReservation={}", id);
        if (repository.existsById(id)) {
            repository.deleteById(id);
            log.info("Reserva eliminada exitosamente: idReservation={}", id);
            return true;
        }
        log.warn("No se pudo eliminar: reserva no encontrada: idReservation={}", id);
        return false;
    }

    @Override
    public List<ReservationResponseDto> findByUserId(Long userId) throws Exception {
        log.info("Buscando reservas por usuario: idUser={}", userId);
        try {
            UserResponseDto userFind = userClient.findById(userId);
            if (userFind == null) {
                log.warn("Usuario no encontrado en ms-usuarios: idUsuario={}", userId);
                return null;
            }
            List<ReservationResponseDto> result = repository.findByIdUser(userId)
                    .stream().map(this::toDto).toList();
            log.info("Reservas encontradas para usuario: idUsuario={}, total={}", userId, result.size());
            return result;
        } catch (Exception e) {
            log.error("Error al buscar reservas por usuario: idUsuario={}, motivo={}", userId, e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public List<ReservationResponseDto> findByCoachId(Long coachId) throws Exception {
        log.info("Buscando reservas por entrenador: idEntrenador={}", coachId);
        try {
            CoachResponseDto coachFind = coachClient.findById(coachId);
            if (coachFind == null) {
                log.warn("Entrenador no encontrado en ms-coaches: idEntrenador={}", coachId);
                return null;
            }
            List<ReservationResponseDto> result = repository.findByIdCoach(coachId)
                    .stream().map(this::toDto).toList();
            log.info("Reservas encontradas para entrenador: idCoach={}, total={}", coachId, result.size());
            return result;
        } catch (Exception e) {
            log.error("Error al buscar reservas por entrenador: idCoach={}, motivo={}", coachId, e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public List<ReservationResponseDto> findByClasseId(Long classeId) throws Exception {
        log.info("Buscando reservas por clase: idClase={}", classeId);
        try {
            ClasseResponseDto classeFind = claseClient.findById(classeId);
            if (classeFind == null) {
                log.warn("Clase no encontrada en ms-classes: idClasse={}", classeId);
                return null;
            }
            List<ReservationResponseDto> result = repository.findByIdClasse(classeId)
                    .stream().map(this::toDto).toList();
            log.info("Reservas encontradas para clase: idClasse={}, total={}", classeId, result.size());
            return result;
        } catch (Exception e) {
            log.error("Error al buscar reservas por clase: idClasse={}, motivo={}", classeId, e.getMessage());
            throw new Exception(e.getMessage());
        }
    }
}