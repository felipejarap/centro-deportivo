package cl.duoc.MS_Usuarios.service.impl;

import cl.duoc.MS_Usuarios.dto.TypeUserResponseDto;
import cl.duoc.MS_Usuarios.dto.UserRequestDto;
import cl.duoc.MS_Usuarios.dto.UserResponseDto;
import cl.duoc.MS_Usuarios.model.TypeUser;
import cl.duoc.MS_Usuarios.model.User;
import cl.duoc.MS_Usuarios.repository.TypeUserRepository;
import cl.duoc.MS_Usuarios.repository.UserRepository;
import cl.duoc.MS_Usuarios.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final TypeUserRepository typeUserRepository;

    private TypeUser resolveTypeUser(Long typeUserId) {
        return typeUserRepository.findById(typeUserId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Tipo de usuario no encontrado: " + typeUserId
                ));
    }

    private User toEntity(UserRequestDto dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPaternalSurname(dto.getPaternalSurname());
        user.setMaternalSurname(dto.getMaternalSurname());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setTypeUser(resolveTypeUser(dto.getTypeUserId()));
        return user;
    }

    private UserResponseDto toDto(User entity) {
        TypeUserResponseDto typeDto = null;
        if (entity.getTypeUser() != null) {
            typeDto = new TypeUserResponseDto(
                    entity.getTypeUser().getId(),
                    entity.getTypeUser().getName()
            );
        }
        return new UserResponseDto(
                entity.getIdUser(),
                entity.getUsername(),
                entity.getPaternalSurname(),
                entity.getMaternalSurname(),
                entity.getEmail(),
                entity.getPhone(),
                typeDto
        );
    }

    @Override
    public List<UserResponseDto> findAll() {
        return repository.findAllWithTypeUser().stream().map(this::toDto).toList();
    }

    @Override
    public UserResponseDto findById(Long id) {
        return repository.findByIdWithTypeUser(id).map(this::toDto).orElse(null);
    }

    @Override
    public List<UserResponseDto> findByTypeUserId(Long typeUserId) {
        if (!typeUserRepository.existsById(typeUserId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Tipo de usuario no encontrado: " + typeUserId
            );
        }
        return repository.findByTypeUser_Id(typeUserId).stream().map(this::toDto).toList();
    }

    @Override
    public UserResponseDto create(UserRequestDto user) {
        User entity = toEntity(user);
        return toDto(repository.save(entity));
    }

    @Override
    public UserResponseDto update(Long id, UserRequestDto user) {
        if (repository.existsById(id)) {
            User entity = toEntity(user);
            entity.setIdUser(id);
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
