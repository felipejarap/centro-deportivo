package cl.duoc.ms.asistencia.service;

import cl.duoc.ms.asistencia.dto.AssistanceRequestDto;
import cl.duoc.ms.asistencia.dto.AssistanceResponseDto;

import java.util.List;

public interface AssistanceService {

    List<AssistanceResponseDto> findAll();
    AssistanceResponseDto findById(Long id);
    AssistanceResponseDto create(AssistanceRequestDto assistance);
    AssistanceResponseDto update(Long id, AssistanceRequestDto assistance);
    boolean delete(Long id);
    List<AssistanceResponseDto> findByUserId(Long userId) throws Exception;
    List<AssistanceResponseDto> findByClasseId(Long classeId) throws Exception;
}
