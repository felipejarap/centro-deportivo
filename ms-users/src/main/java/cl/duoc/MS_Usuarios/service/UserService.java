package cl.duoc.MS_Usuarios.service;

import cl.duoc.MS_Usuarios.dto.UserRequestDto;
import cl.duoc.MS_Usuarios.dto.UserResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;


public interface UserService {

    List<UserResponseDto> findAll();
    UserResponseDto findById(Long id);
    List<UserResponseDto> findByTypeUserId(Long typeUserId);
    UserResponseDto create(UserRequestDto user);
    UserResponseDto update(Long id, UserRequestDto user);
    boolean delete(Long id);
}
