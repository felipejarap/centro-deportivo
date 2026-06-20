package cl.duoc.ms_reservas.service;

import cl.duoc.ms_reservas.dto.ReservationRequestDto;
import cl.duoc.ms_reservas.dto.ReservationResponseDto;

import java.util.List;

public interface ReservationService {

    List<ReservationResponseDto> findAll();
    ReservationResponseDto findById(Long id);
    ReservationResponseDto create(ReservationRequestDto reservation);
    ReservationResponseDto update(Long id, ReservationRequestDto reservation);
    boolean delete(Long id);
    List<ReservationResponseDto> findByUserId(Long userId) throws Exception;
    List<ReservationResponseDto>findByCoachId(Long coachId) throws Exception;
    List<ReservationResponseDto>findByClasseId(Long classeId) throws Exception;
}
