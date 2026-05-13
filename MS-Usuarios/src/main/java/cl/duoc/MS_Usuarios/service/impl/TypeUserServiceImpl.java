package cl.duoc.MS_Usuarios.service.impl;

import cl.duoc.MS_Usuarios.dto.TypeUserRequestDto;
import cl.duoc.MS_Usuarios.dto.TypeUserResponseDto;
import cl.duoc.MS_Usuarios.model.TypeUser;
import cl.duoc.MS_Usuarios.repository.TypeUserRepository;
import cl.duoc.MS_Usuarios.service.TypeUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TypeUserServiceImpl implements TypeUserService {

    private final TypeUserRepository repository;

    private TypeUserResponseDto toDto(TypeUser e) {
        return new TypeUserResponseDto(e.getId(), e.getName());
    }

    @Override
    public List<TypeUserResponseDto> findAll() {
        return repository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public TypeUserResponseDto findById(Long id) {
        return repository.findById(id).map(this::toDto).orElse(null);
    }

    @Override
    public TypeUserResponseDto create(TypeUserRequestDto dto) {
        TypeUser e = new TypeUser();
        e.setName(dto.getName());
        return toDto(repository.save(e));
    }
    @Override
    public TypeUserResponseDto update(Long id, TypeUserRequestDto dto) {
        if (repository.existsById(id)) {
            TypeUser e = new TypeUser();
            e.setId(id);
            e.setName(dto.getName());
            return toDto(repository.save(e));
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
