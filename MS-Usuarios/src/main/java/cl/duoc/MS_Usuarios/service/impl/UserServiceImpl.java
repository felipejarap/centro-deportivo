package cl.duoc.MS_Usuarios.service.impl;

import cl.duoc.MS_Usuarios.dto.TypeUserResponseDto;
import cl.duoc.MS_Usuarios.dto.UserRequestDto;
import cl.duoc.MS_Usuarios.dto.UserResponseDto;
import cl.duoc.MS_Usuarios.model.User;
import cl.duoc.MS_Usuarios.repository.TypeUserRepository;
import cl.duoc.MS_Usuarios.repository.UserRepository;
import cl.duoc.MS_Usuarios.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final TypeUserRepository typeUserRepository;


    private User toEntity(UserRequestDto dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setAppaterno(dto.getAppaterno());
        user.setApmaterno(dto.getApmaterno());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        if(dto.getTypeUserId() != null){ typeUserRepository.findById(dto.getTypeUserId())
                .ifPresent(user::setTypeUser);
        }
        return user;
    }
    private UserResponseDto toDto(User entity){
        TypeUserResponseDto  typeDto=null;
        if(entity.getTypeUser()!=null){
            typeDto = new TypeUserResponseDto(
                    entity.getTypeUser().getId(),
                    entity.getTypeUser().getName()
            );
        }
        return new UserResponseDto(
                entity.getIdUser(),
                entity.getUsername(),
                entity.getAppaterno(),
                entity.getApmaterno(),
                entity.getEmail(),
                entity.getPhone(),
                typeDto
        );
    }


    @Override
    public List<UserResponseDto> findAll() {
        return repository.findAll().stream().map(this::toDto).toList();

    }

    @Override
    public UserResponseDto findById(Long id) {

        return repository.findById(id).map(this::toDto).orElse(null);
    }

    @Override
    public UserResponseDto create(UserRequestDto user) {
       User entity = toEntity(user);
       return toDto(repository.save(entity));
    }

    @Override
    public UserResponseDto update(Long id, UserRequestDto user) {
        if(repository.existsById(id)){
            User entity = toEntity(user);
            entity.setIdUser(id);
            return toDto(repository.save(entity));
        }
        return null;
    }

    @Override
    public boolean delete(Long id) {
        if(repository.existsById(id)){
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
