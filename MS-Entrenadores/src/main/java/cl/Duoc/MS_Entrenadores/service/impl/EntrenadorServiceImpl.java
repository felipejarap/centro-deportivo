package cl.Duoc.MS_Entrenadores.service.impl;

import cl.Duoc.MS_Entrenadores.dto.EntrenadorRequestDto;
import cl.Duoc.MS_Entrenadores.dto.EntrenadorResponseDto;
import cl.Duoc.MS_Entrenadores.repository.EntrenadorRepository;
import cl.Duoc.MS_Entrenadores.service.EntrenadorService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import cl.Duoc.MS_Entrenadores.model.Entrenador;

import java.util.List;
@Service
@RequiredArgsConstructor
public class EntrenadorServiceImpl implements EntrenadorService {

    private final EntrenadorRepository repository;

    private Entrenador toEntity(EntrenadorRequestDto dto){
        Entrenador e = new Entrenador();
        e.setNombre(dto.getNombre());
        e.setAppaterno(dto.getAppaterno());
        e.setApmaterno(dto.getApmaterno());
        e.setEspecialidad(dto.getEspecialidad());
        e.setCertificacion(dto.getCertificacion());
        return e;

    }

    private EntrenadorResponseDto toDto(Entrenador entity){
        return new EntrenadorResponseDto(
                entity.getIdEntrenador(),
                entity.getNombre(),
                entity.getAppaterno(),
                entity.getApmaterno(),
                entity.getEspecialidad(),
                entity.getCertificacion()

        );
    }




    @Override
    public List<EntrenadorResponseDto> findAll() {

        return repository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public EntrenadorResponseDto findById(Long id) {
        return repository.findById(id).map(this::toDto).orElse(null);
    }

    @Override
    public EntrenadorResponseDto create(EntrenadorRequestDto entrenador) {
        return toDto(repository.save(toEntity(entrenador)));
    }

    @Override
    public EntrenadorResponseDto update(Long id, EntrenadorRequestDto entrenador) {
        if (repository.existsById(id)) {
            Entrenador entity = toEntity(entrenador);
            entity.setIdEntrenador(id);
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
}
