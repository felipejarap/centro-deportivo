package cl.duoc.ms_reservas.service;

import cl.duoc.ms_reservas.dto.ReservaRequestDto;
import cl.duoc.ms_reservas.dto.ReservaResponseDto;
import cl.duoc.ms_reservas.model.Reserva;

import java.util.List;

public interface ReservaService {

    List<ReservaResponseDto> findAll();
    ReservaResponseDto findById(Long id);
    ReservaResponseDto create(ReservaRequestDto reserva);
    ReservaResponseDto update(Long id, ReservaRequestDto reserva);
    boolean delete(Long id);
    List<ReservaResponseDto> findByUserId(Long userId) throws Exception;
    List<ReservaResponseDto>findByEntrenadorId(Long entrenadorId) throws Exception;
    List<ReservaResponseDto>findByClasseId(Long classeId) throws Exception;
}
