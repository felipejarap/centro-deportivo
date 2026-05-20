package cl.duoc.ms_rutinaEjercicio.service.impl;


import cl.duoc.ms_rutinaEjercicio.dto.EntrenadorResponseDto;
import cl.duoc.ms_rutinaEjercicio.dto.RutinaEjercicioRequestDto;
import cl.duoc.ms_rutinaEjercicio.dto.RutinaEjercicioResponseDto;
import cl.duoc.ms_rutinaEjercicio.dto.UserResponseDto;
import cl.duoc.ms_rutinaEjercicio.model.RutinaEjercicio;
import cl.duoc.ms_rutinaEjercicio.respository.RutinaEjercicioRepository;
import cl.duoc.ms_rutinaEjercicio.service.RutinaEjercicioService;
import cl.duoc.ms_rutinaEjercicio.service.api.EntrenadorClient;
import cl.duoc.ms_rutinaEjercicio.service.api.UsuarioClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RutinaEjercicioServiceImpl implements RutinaEjercicioService {

    private final RutinaEjercicioRepository repository;
    private final UsuarioClient usuarioClient;
    private final EntrenadorClient entrenadorClient;

    private RutinaEjercicio toEntity(RutinaEjercicioRequestDto dto) {
        RutinaEjercicio r = new RutinaEjercicio();
        r.setIdUsuario(dto.getIdUsuario());
        r.setIdEntrenador(dto.getIdEntrenador());
        r.setNombre(dto.getNombre());
        r.setDescripcion(dto.getDescripcion());
        r.setObjetivo(dto.getObjetivo());
        r.setPesoRegistrado(dto.getPesoRegistrado());
        r.setMarcaPersonal(dto.getMarcaPersonal());
        r.setFechaAsignacion(dto.getFechaAsignacion());
        r.setActiva(dto.getActiva());
        return r;
    }

    private RutinaEjercicioResponseDto toDto(RutinaEjercicio entity) {
        RutinaEjercicioResponseDto dto = new RutinaEjercicioResponseDto();
        dto.setIdRutina(entity.getIdRutina());
        dto.setIdUsuario(entity.getIdUsuario());
        dto.setIdEntrenador(entity.getIdEntrenador());
        dto.setNombre(entity.getNombre());
        dto.setDescripcion(entity.getDescripcion());
        dto.setObjetivo(entity.getObjetivo());
        dto.setPesoRegistrado(entity.getPesoRegistrado());
        dto.setMarcaPersonal(entity.getMarcaPersonal());
        dto.setFechaAsignacion(entity.getFechaAsignacion());
        dto.setActiva(entity.getActiva());

        try {
            UserResponseDto usuario = usuarioClient.findById(entity.getIdUsuario());
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

        return dto;
    }

    @Override
    public List<RutinaEjercicioResponseDto> findAll() {
        return repository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public RutinaEjercicioResponseDto findById(Long id) {
        return repository.findById(id).map(this::toDto).orElse(null);
    }

    @Override
    public RutinaEjercicioResponseDto create(RutinaEjercicioRequestDto rutina) {
        UserResponseDto userFind = usuarioClient.findById(rutina.getIdUsuario());
        if(userFind == null || userFind.getId()==null){
            throw new IllegalArgumentException("El usuario con ID " +rutina.getIdUsuario()+ "no existe ");
        }
        EntrenadorResponseDto entrenadorFind = entrenadorClient.findById(rutina.getIdEntrenador());
        if(entrenadorFind == null || entrenadorFind.getId()==null){
            throw new IllegalArgumentException("El Entrenador con ID " +rutina.getIdEntrenador()+ "no existe ");
        }
        return toDto(repository.save(toEntity(rutina)));
    }

    @Override
    public RutinaEjercicioResponseDto update(Long id, RutinaEjercicioRequestDto rutina) {
        UserResponseDto userFind = usuarioClient.findById(rutina.getIdUsuario());
        if(userFind == null || userFind.getId()==null){
            throw new IllegalArgumentException("El usuario con ID " +rutina.getIdUsuario()+ "no existe ");
        }
        EntrenadorResponseDto entrenadorFind = entrenadorClient.findById(rutina.getIdEntrenador());
        if(entrenadorFind == null || entrenadorFind.getId()==null){
            throw new IllegalArgumentException("El Entrenador con ID " +rutina.getIdEntrenador()+ "no existe ");
        }
        if (repository.existsById(id)) {
            RutinaEjercicio entity = toEntity(rutina);
            entity.setIdRutina(id);
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
    public List<RutinaEjercicioResponseDto> findByUserId(Long userId) throws Exception {
        try {
            UserResponseDto userFind = usuarioClient.findById(userId);
            if (userFind == null) return null;
            return repository.findByIdUsuario(userId).stream().map(this::toDto).toList();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public List<RutinaEjercicioResponseDto> findByEntrenadorId(Long entrenadorId) throws Exception {
        try {
            EntrenadorResponseDto entrenadorFind = entrenadorClient.findById(entrenadorId);
            if (entrenadorFind == null) return null;
            return repository.findByIdEntrenador(entrenadorId).stream().map(this::toDto).toList();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public List<RutinaEjercicioResponseDto> findByObjetivo(String objetivo) {
        return repository.findByObjetivo(objetivo).stream().map(this::toDto).toList();
    }

    @Override
    public List<RutinaEjercicioResponseDto> findActivasByUserId(Long userId) throws Exception {
        try {
            UserResponseDto userFind = usuarioClient.findById(userId);
            if (userFind == null) return null;
            return repository.findByIdUsuarioAndActiva(userId, true).stream().map(this::toDto).toList();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}