package cl.duoc.ms_reservas.service.impl;


import cl.duoc.ms_reservas.dto.*;
import cl.duoc.ms_reservas.model.Reserva;
import cl.duoc.ms_reservas.repository.ReservaRespository;
import cl.duoc.ms_reservas.service.ReservaService;
import cl.duoc.ms_reservas.service.api.ClasseClient;
import cl.duoc.ms_reservas.service.api.EntrenadorClient;
import cl.duoc.ms_reservas.service.api.UsuarioClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservaServiceImpl implements ReservaService {
    private final ReservaRespository repository;
    private final UsuarioClient usuarioClient;
    private final EntrenadorClient entrenadorClient;
    private final ClasseClient claseClient;

    private Reserva toEntity(ReservaRequestDto dto) {
        Reserva r = new Reserva();
        r.setIdUsuario(dto.getIdUsuario());  // ← corregido
        r.setIdClase(dto.getIdClase());
        r.setIdEntrenador(dto.getIdEntrenador());
        r.setFechaReserva(dto.getFechaReserva());
        r.setEstadoReserva(dto.getEstadoReserva());
        return r;
    }

    private ReservaResponseDto toDto(Reserva entity) {
        ReservaResponseDto dto = new ReservaResponseDto();
        dto.setIdReserva(entity.getIdReserva());
        dto.setIdUsuario(entity.getIdUsuario());  // ← corregido
        dto.setIdClase(entity.getIdClase());
        dto.setIdEntrenador(entity.getIdEntrenador());
        dto.setFechaReserva(entity.getFechaReserva());
        dto.setEstadoReserva(entity.getEstadoReserva());

        try {
            UserResponseDto usuario = usuarioClient.findById(entity.getIdUsuario());  // ← corregido
            dto.setUsuario(usuario);
        } catch (Exception e) {
            dto.setUsuario(null);
        }

        try {
            EntrenadorResponseDto entrenador = entrenadorClient.findById(entity.getIdEntrenador());
            dto.setEntrenador(entrenador);
        } catch (Exception e) {
            dto.setEntrenador(null);
        }

        try {
            ClasseResponseDto clase = claseClient.findById(entity.getIdClase());
            dto.setClase(clase);
        } catch (Exception e) {
            dto.setClase(null);
        }

        return dto;
    }

    @Override
    public List<ReservaResponseDto> findAll() {
        return repository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public ReservaResponseDto findById(Long id) {
        return repository.findById(id).map(this::toDto).orElse(null);
    }

    @Override
    public ReservaResponseDto create(ReservaRequestDto reserva) {
        return toDto(repository.save(toEntity(reserva)));
    }

    @Override
    public ReservaResponseDto update(Long id, ReservaRequestDto reserva) {
        if (repository.existsById(id)) {
            Reserva entity = toEntity(reserva);
            entity.setIdReserva(id);
            return toDto(repository.save(entity));
        }
        return null;
    }

    @Override
    public boolean delete(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public List<ReservaResponseDto> finByUserId(Long UserId) throws Exception {
        try{
            UserResponseDto userFind = usuarioClient.findById(UserId);
                if(userFind == null){
                    return null;
                }
            return repository.findByUserId(UserId).stream().map(this::toDto).toList();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

    }

    @Override
    public List<ReservaResponseDto> findByEntrenadorId(Long EntrenadorId)throws Exception {
        try{
            EntrenadorResponseDto entrenadorFind = entrenadorClient.findById(EntrenadorId);
            if(entrenadorFind == null){
                return null;
            }
            return repository.findByEntrenadorId(EntrenadorId).stream().map(this::toDto).toList();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }


    }

    @Override
    public List<ReservaResponseDto> findByClasseId(Long ClasseId) throws Exception {
        try{
            ClasseResponseDto classeFind = claseClient.findById(ClasseId);
            if(classeFind == null){
                return null;
            }
            return repository.findByClasseId(ClasseId).stream().map(this::toDto).toList();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

}
