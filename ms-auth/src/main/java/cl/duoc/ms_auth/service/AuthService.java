package cl.duoc.ms_auth.service;

import cl.duoc.ms_auth.dto.AuthRequestDto;
import cl.duoc.ms_auth.dto.AuthResponseDto;
import cl.duoc.ms_auth.dto.CredencialRequestDto;
import cl.duoc.ms_auth.dto.CredencialResponseDto;

import java.util.List;

public interface AuthService {
    AuthResponseDto login(AuthRequestDto request);
    CredencialResponseDto register(CredencialRequestDto request);
    List<CredencialResponseDto> findAll();
    CredencialResponseDto findById(Long id);
    boolean delete(Long id);
}
