package cl.duoc.MS_Usuarios.service.impl;

import cl.duoc.MS_Usuarios.dto.TypeUserRequestDto;
import cl.duoc.MS_Usuarios.dto.TypeUserResponseDto;
import cl.duoc.MS_Usuarios.model.TypeUser;
import cl.duoc.MS_Usuarios.repository.TypeUserRepository;
import cl.duoc.MS_Usuarios.repository.UserRepository;
import cl.duoc.MS_Usuarios.service.TypeUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TypeUserServiceImpl implements TypeUserService {

    private final TypeUserRepository repository;
    private final UserRepository userRepository;

    private TypeUserResponseDto toDto(TypeUser entity) {
        return new TypeUserResponseDto(entity.getId(), entity.getName());
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
        repository.findByNameIgnoreCase(dto.getName()).ifPresent(existing -> {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ya existe un tipo de usuario con el nombre: " + dto.getName()
            );
        });

        TypeUser entity = new TypeUser();
        entity.setName(dto.getName());
        return toDto(repository.save(entity));
    }

    @Override
    public TypeUserResponseDto update(Long id, TypeUserRequestDto dto) {
        TypeUser existing = repository.findById(id).orElse(null);
        if (existing == null) {
            return null;
        }

        repository.findByNameIgnoreCase(dto.getName()).ifPresent(duplicate -> {
            if (!duplicate.getId().equals(id)) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Ya existe un tipo de usuario con el nombre: " + dto.getName()
                );
            }
        });

        existing.setName(dto.getName());
        return toDto(repository.save(existing));
    }

    @Override
    public boolean deleteById(Long id) {
        if (!repository.existsById(id)) {
            return false;
        }
        if (!userRepository.findByTypeUser_Id(id).isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "No se puede eliminar un tipo de usuario asignado a usuarios existentes"
            );
        }
        repository.deleteById(id);
        return true;
    }
}
