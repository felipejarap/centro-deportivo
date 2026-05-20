package cl.duoc.ms.asistencia.service;

import cl.duoc.ms.asistencia.dto.AsistenciaRequestDto;
import cl.duoc.ms.asistencia.dto.AsistenciaResponseDto;

import java.util.List;

public interface AsistenciaService {

    List<AsistenciaResponseDto> findAll();
    AsistenciaResponseDto findById(Long id);
    AsistenciaResponseDto create(AsistenciaRequestDto asistencia);
    AsistenciaResponseDto update(Long id, AsistenciaRequestDto asistencia);
    boolean delete(Long id);
    List<AsistenciaResponseDto> findByUserId(Long userId) throws Exception;
    List<AsistenciaResponseDto> findByClasseId(Long classeId) throws Exception;
}
