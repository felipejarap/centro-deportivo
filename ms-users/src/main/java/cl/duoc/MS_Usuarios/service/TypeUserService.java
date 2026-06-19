package cl.duoc.MS_Usuarios.service;

import cl.duoc.MS_Usuarios.dto.TypeUserRequestDto;
import cl.duoc.MS_Usuarios.dto.TypeUserResponseDto;

import java.util.List;

public interface TypeUserService {
    List<TypeUserResponseDto> findAll();
    TypeUserResponseDto findById(Long id);
    TypeUserResponseDto create(TypeUserRequestDto type);
    TypeUserResponseDto update(Long id, TypeUserRequestDto type);
    boolean deleteById(Long id);
}
