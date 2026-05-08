package duoc.cl.ms_classes.service.impl;

import duoc.cl.ms_classes.dto.ClasseRequestDto;
import duoc.cl.ms_classes.dto.ClasseResponseDto;
import duoc.cl.ms_classes.dto.TypeClassesRequestDto;
import duoc.cl.ms_classes.dto.TypeClassesResponseDto;
import duoc.cl.ms_classes.model.Classe;
import duoc.cl.ms_classes.model.TypeClasses;
import duoc.cl.ms_classes.repository.ClasseRepository;
import duoc.cl.ms_classes.service.ClasseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClasseServiceImpl implements ClasseService {

    private final ClasseRepository repository;

    private Classe toEntity(ClasseResponseDto dto) {
        return new Classe(dto.getId(),dto.getStartDate(),dto.getEndDate(),dto.getMaximumCapacity(),dto.getSpotsAvailable(),dto.getTypeClasse());
    }

    private Classe toEntity(ClasseRequestDto dto) {
        return new Classe(dto.getId(),dto.getStartDate(),dto.getEndDate(),dto.getMaximumCapacity(),dto.getSpotsAvailable(),dto.getTypeClasse());

    }

    private ClasseResponseDto toDto(Classe entity) {
        return new ClasseResponseDto(entity.getId(),entity.getStartDate(),entity.getEndDate(),entity.getMaximumCapacity(),entity.getSpotsAvailable(),entity.getTypeClasse());
    }


    private TypeClasses toEntity(TypeClassesResponseDto dto) {
        return new TypeClasses( dto.getId(),dto.getName());
    }
    private TypeClasses toEntity(TypeClassesRequestDto dto) {
        return new TypeClasses( dto.getId(),dto.getName());
    }

    private TypeClassesResponseDto toDto(TypeClasses entity) {
        return new TypeClassesResponseDto(entity.getId(),entity.getName());
    }

    @Override
    public List<ClasseResponseDto> findAll() {
        return repository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public ClasseResponseDto findById(Long id) {
        return repository.findById(id).map(this::toDto).orElse(null);
    }

    @Override
    public ClasseResponseDto create(ClasseRequestDto classe) {
        return toDto(repository.save(toEntity(classe)));
    }

    @Override
    public ClasseResponseDto update(Long id, ClasseRequestDto classe) {
        if (repository.existsById(id)) {
            Classe entity = toEntity(classe);
            entity.setId(id);
            return toDto(repository.save(entity));
        }
        return null;
    }

    @Override
    public boolean deleteById(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
