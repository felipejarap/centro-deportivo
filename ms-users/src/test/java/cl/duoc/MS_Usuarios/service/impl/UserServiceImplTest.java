package cl.duoc.MS_Usuarios.service.impl;

import cl.duoc.MS_Usuarios.dto.UserRequestDto;
import cl.duoc.MS_Usuarios.dto.UserResponseDto;
import cl.duoc.MS_Usuarios.model.TypeUser;
import cl.duoc.MS_Usuarios.model.User;
import cl.duoc.MS_Usuarios.repository.TypeUserRepository;
import cl.duoc.MS_Usuarios.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository repository;

    @Mock
    private TypeUserRepository typeUserRepository;

    @InjectMocks
    private UserServiceImpl service;

    private User userEntity;
    private TypeUser typeUserEntity;
    private UserRequestDto requestDto;

    @BeforeEach
    void setUp() {
        typeUserEntity = new TypeUser();
        typeUserEntity.setId(1L);
        typeUserEntity.setName("Admin");

        userEntity = new User();
        userEntity.setIdUser(1L);
        userEntity.setUsername("Eduardo");
        userEntity.setPaternalSurname("Urquieta");
        userEntity.setMaternalSurname("Cruz");
        userEntity.setEmail("ed.urquieta@duocuc.cl");
        userEntity.setPhone("+56912345678");
        userEntity.setTypeUser(typeUserEntity);

        requestDto = new UserRequestDto();
        requestDto.setUsername("Eduardo");
        requestDto.setPaternalSurname("Urquieta");
        requestDto.setMaternalSurname("Cruz");
        requestDto.setEmail("ed.urquieta@duocuc.cl");
        requestDto.setPhone("+56912345678");
        requestDto.setTypeUserId(1L);
    }

    // ==========================================
    // PRUEBAS: findAll() y findById()
    // ==========================================
    @Test
    void givenExistingUsers_whenFindAll_thenReturnList() {
        when(repository.findAllWithTypeUser()).thenReturn(List.of(userEntity));
        List<UserResponseDto> result = service.findAll();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Eduardo", result.get(0).getUsername());
    }

    @Test
    void givenExistingId_whenFindById_thenReturnDto() {
        when(repository.findByIdWithTypeUser(1L)).thenReturn(Optional.of(userEntity));
        UserResponseDto result = service.findById(1L);
        assertNotNull(result);
        assertEquals("Eduardo", result.getUsername());
    }

    // ==========================================
    // PRUEBAS: findByTypeUserId()
    // ==========================================
    @Test
    void givenExistingTypeUserId_whenFindByTypeUserId_thenReturnList() {
        when(typeUserRepository.existsById(1L)).thenReturn(true);
        when(repository.findByTypeUser_Id(1L)).thenReturn(List.of(userEntity));

        List<UserResponseDto> result = service.findByTypeUserId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void givenNonExistingTypeUserId_whenFindByTypeUserId_throwNotFoundException() {
        when(typeUserRepository.existsById(99L)).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            service.findByTypeUserId(99L);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(repository, never()).findByTypeUser_Id(anyLong());
    }

    // ==========================================
    // PRUEBAS: create()
    // ==========================================
    @Test
    void givenValidRequest_whenCreate_thenReturnCreatedDto() {
        when(typeUserRepository.findById(1L)).thenReturn(Optional.of(typeUserEntity));
        when(repository.save(any(User.class))).thenReturn(userEntity);

        UserResponseDto result = service.create(requestDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(repository, times(1)).save(any(User.class));
    }

    @Test
    void givenInvalidTypeUserId_whenCreate_throwBadRequestException() {
        requestDto.setTypeUserId(99L);
        when(typeUserRepository.findById(99L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            service.create(requestDto);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        verify(repository, never()).save(any(User.class));
    }

    // ==========================================
    // PRUEBAS: update()
    // ==========================================
    @Test
    void givenExistingIdAndValidRequest_whenUpdate_thenReturnUpdatedDto() {
        when(repository.existsById(1L)).thenReturn(true);
        when(typeUserRepository.findById(1L)).thenReturn(Optional.of(typeUserEntity));
        when(repository.save(any(User.class))).thenReturn(userEntity);

        UserResponseDto result = service.update(1L, requestDto);

        assertNotNull(result);
        verify(repository, times(1)).save(any(User.class));
    }

    @Test
    void givenNonExistingUserId_whenUpdate_thenReturnNull() {
        when(repository.existsById(99L)).thenReturn(false);

        UserResponseDto result = service.update(99L, requestDto);

        assertNull(result);
        verify(repository, never()).save(any(User.class));
    }

    // ==========================================
    // PRUEBAS: delete()
    // ==========================================
    @Test
    void givenExistingUserId_whenDelete_thenReturnTrue() {
        when(repository.existsById(1L)).thenReturn(true);
        doNothing().when(repository).deleteById(1L);

        boolean result = service.delete(1L);

        assertTrue(result);
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void givenNonExistingUserId_whenDelete_thenReturnFalse() {
        when(repository.existsById(99L)).thenReturn(false);

        boolean result = service.delete(99L);

        assertFalse(result);
        verify(repository, never()).deleteById(anyLong());
    }
}
