package cl.duoc.ms_rutinaEjercicio.service;

import cl.duoc.ms_rutinaEjercicio.dto.RutinaEjercicioRequestDto;
import cl.duoc.ms_rutinaEjercicio.dto.RutinaEjercicioResponseDto;

import java.util.List;

public interface RutinaEjercicioService {
    List<RutinaEjercicioResponseDto> findAll();
    RutinaEjercicioResponseDto findById(Long id);
    RutinaEjercicioResponseDto create(RutinaEjercicioRequestDto rutina);
    RutinaEjercicioResponseDto update(Long id, RutinaEjercicioRequestDto rutina);
    boolean delete(Long id);
    List<RutinaEjercicioResponseDto> findByUserId(Long userId) throws Exception;
    List<RutinaEjercicioResponseDto> findByEntrenadorId(Long entrenadorId) throws Exception;
    List<RutinaEjercicioResponseDto> findByObjetivo(String objetivo);
    List<RutinaEjercicioResponseDto> findActivasByUserId(Long userId) throws Exception;
}
