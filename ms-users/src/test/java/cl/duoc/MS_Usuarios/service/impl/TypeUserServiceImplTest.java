package cl.duoc.MS_Usuarios.service.impl;

import cl.duoc.MS_Usuarios.dto.TypeUserRequestDto;
import cl.duoc.MS_Usuarios.dto.TypeUserResponseDto;
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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TypeUserServiceImplTest {

    @Mock
    private TypeUserRepository repository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TypeUserServiceImpl service;

    private TypeUser typeUserEntity;
    private TypeUserRequestDto requestDto;

    @BeforeEach
    void setUp() {
        typeUserEntity = new TypeUser();
        typeUserEntity.setId(1L);
        typeUserEntity.setName("Admin");

        requestDto = new TypeUserRequestDto();
        requestDto.setName("Admin");
    }


    @Test
    void givenExistingRecords_whenFindAll_thenReturnList() {
        when(repository.findAll()).thenReturn(List.of(typeUserEntity));
        List<TypeUserResponseDto> result = service.findAll();
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void givenExistingId_whenFindById_thenReturnDto() {
        when(repository.findById(1L)).thenReturn(Optional.of(typeUserEntity));
        TypeUserResponseDto result = service.findById(1L);
        assertNotNull(result);
        assertEquals("Admin", result.getName());
    }

    // ==========================================
    // PRUEBAS: create() (Camino Feliz y Conflicto)
    // ==========================================
    @Test
    void givenNewName_whenCreate_thenReturnCreatedDto() {
        when(repository.findByNameIgnoreCase("Admin")).thenReturn(Optional.empty());
        when(repository.save(any(TypeUser.class))).thenReturn(typeUserEntity);

        TypeUserResponseDto result = service.create(requestDto);

        assertNotNull(result);
        assertEquals("Admin", result.getName());
    }

    @Test
    void givenDuplicateName_whenCreate_throwConflictException() {
        when(repository.findByNameIgnoreCase("Admin")).thenReturn(Optional.of(typeUserEntity));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            service.create(requestDto);
        });

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        assertTrue(exception.getReason().contains("Ya existe un tipo de usuario"));
        verify(repository, never()).save(any(TypeUser.class));
    }


    @Test
    void givenValidIdAndRequest_whenUpdate_thenReturnUpdatedDto() {
        when(repository.findById(1L)).thenReturn(Optional.of(typeUserEntity));
        when(repository.findByNameIgnoreCase("Admin")).thenReturn(Optional.empty());
        when(repository.save(any(TypeUser.class))).thenReturn(typeUserEntity);

        TypeUserResponseDto result = service.update(1L, requestDto);

        assertNotNull(result);
        verify(repository, times(1)).save(any(TypeUser.class));
    }

    @Test
    void givenNonExistingId_whenUpdate_thenReturnNull() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        TypeUserResponseDto result = service.update(99L, requestDto);

        assertNull(result);
        verify(repository, never()).save(any(TypeUser.class));
    }

    @Test
    void givenDuplicateNameOtherId_whenUpdate_throwConflictException() {
        TypeUser secondaryEntity = new TypeUser();
        secondaryEntity.setId(2L); // ID diferente provoca conflicto de duplicado
        secondaryEntity.setName("Admin");

        when(repository.findById(1L)).thenReturn(Optional.of(typeUserEntity));
        when(repository.findByNameIgnoreCase("Admin")).thenReturn(Optional.of(secondaryEntity));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            service.update(1L, requestDto);
        });

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
    }


    @Test
    void givenExistingIdAndNoUsersAssigned_whenDeleteById_thenReturnTrue() {
        when(repository.existsById(1L)).thenReturn(true);
        when(userRepository.findByTypeUser_Id(1L)).thenReturn(Collections.emptyList());

        boolean result = service.deleteById(1L);

        assertTrue(result);
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void givenNonExistingId_whenDeleteById_thenReturnFalse() {
        when(repository.existsById(99L)).thenReturn(false);

        boolean result = service.deleteById(99L);

        assertFalse(result);
        verify(repository, never()).deleteById(anyLong());
    }

    @Test
    void givenIdWithAssignedUsers_whenDeleteById_throwConflictException() {
        when(repository.existsById(1L)).thenReturn(true);
        when(userRepository.findByTypeUser_Id(1L)).thenReturn(List.of(new User())); // Simula que tiene usuarios amarrados

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            service.deleteById(1L);
        });

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        assertTrue(exception.getReason().contains("No se puede eliminar"));
        verify(repository, never()).deleteById(anyLong());
    }
}
