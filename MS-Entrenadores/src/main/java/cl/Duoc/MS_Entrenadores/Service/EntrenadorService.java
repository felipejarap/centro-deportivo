package cl.Duoc.MS_Entrenadores.service;

import cl.Duoc.MS_Entrenadores.dto.EntrenadorRequestDto;
import cl.Duoc.MS_Entrenadores.dto.EntrenadorResponseDto;

import java.util.List;

public interface EntrenadorService {
    List<EntrenadorResponseDto> findAll();
    EntrenadorResponseDto findById(Long id);
    EntrenadorResponseDto create (EntrenadorRequestDto entrenador);
    EntrenadorResponseDto update(Long id, EntrenadorRequestDto entrenador);
    boolean delete(Long id);


}
