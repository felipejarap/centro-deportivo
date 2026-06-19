package cl.Duoc.MS_Entrenadores.service.impl;

import cl.Duoc.MS_Entrenadores.dto.CoachRequestDto;
import cl.Duoc.MS_Entrenadores.dto.CoachResponseDto;
import cl.Duoc.MS_Entrenadores.repository.CoachRepository;
import cl.Duoc.MS_Entrenadores.service.CoachService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import cl.Duoc.MS_Entrenadores.model.Coach;

import java.util.List;
@Service
@RequiredArgsConstructor
public class CoachServiceImpl implements CoachService {

    private final CoachRepository repository;

    private Coach toEntity(CoachRequestDto dto){
        Coach e = new Coach();
        e.setName(dto.getName());
        e.setPaternalSurname(dto.getPaternalSurname());
        e.setMaternalSurname(dto.getMaternalSurname());
        e.setSpecialty(dto.getSpecialty());
        e.setCertification(dto.getCertification());
        return e;

    }

    private CoachResponseDto toDto(Coach entity){
        return new CoachResponseDto(
                entity.getIdCoach(),
                entity.getName(),
                entity.getPaternalSurname(),
                entity.getMaternalSurname(),
                entity.getSpecialty(),
                entity.getCertification()

        );
    }




    @Override
    public List<CoachResponseDto> findAll() {

        return repository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public CoachResponseDto findById(Long id) {
        return repository.findById(id).map(this::toDto).orElse(null);
    }

    @Override
    public CoachResponseDto create(CoachRequestDto coach) {
        return toDto(repository.save(toEntity(coach)));
    }

    @Override
    public CoachResponseDto update(Long id, CoachRequestDto coach) {
        if (repository.existsById(id)) {
            Coach entity = toEntity(coach);
            entity.setIdCoach(id);
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
