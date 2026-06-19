package cl.Duoc.MS_Entrenadores.service;

import cl.Duoc.MS_Entrenadores.dto.CoachRequestDto;
import cl.Duoc.MS_Entrenadores.dto.CoachResponseDto;

import java.util.List;

public interface CoachService {
    List<CoachResponseDto> findAll();
    CoachResponseDto findById(Long id);
    CoachResponseDto create (CoachRequestDto coach);
    CoachResponseDto update(Long id, CoachRequestDto coach);
    boolean delete(Long id);


}
