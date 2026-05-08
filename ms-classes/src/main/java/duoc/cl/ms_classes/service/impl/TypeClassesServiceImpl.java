package duoc.cl.ms_classes.service.impl;

import duoc.cl.ms_classes.dto.TypeClassesRequestDto;
import duoc.cl.ms_classes.dto.TypeClassesResponseDto;
import duoc.cl.ms_classes.model.TypeClasses;
import duoc.cl.ms_classes.repository.TypeClassesRepository;
import duoc.cl.ms_classes.service.TypeClassesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TypeClassesServiceImpl implements TypeClassesService {
    private final TypeClassesRepository repository;

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
    public List<TypeClassesResponseDto> findAll() {
        return repository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public TypeClassesResponseDto findById(Long id) {
       return repository.findById(id).map(this::toDto).orElse(null);
    }

    @Override
    public TypeClassesResponseDto create(TypeClassesRequestDto type) {
        return toDto(repository.save(toEntity(type)));
    }

    @Override
    public TypeClassesResponseDto update(Long id, TypeClassesRequestDto type) {
        if (repository.existsById(id)) {
            TypeClasses entity = toEntity(type);
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
