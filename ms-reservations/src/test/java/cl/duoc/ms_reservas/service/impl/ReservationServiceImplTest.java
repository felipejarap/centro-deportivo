package cl.duoc.ms_reservas.service.impl;

import cl.duoc.ms_reservas.dto.*;
import cl.duoc.ms_reservas.model.Reservation;
import cl.duoc.ms_reservas.repository.ReservationRespository;
import cl.duoc.ms_reservas.service.api.ClasseClient;
import cl.duoc.ms_reservas.service.api.CoachClient;
import cl.duoc.ms_reservas.service.api.UserClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceImplTest {

    @Mock
    private ReservationRespository repository;

    @Mock
    private UserClient userClient;

    @Mock
    private CoachClient coachClient;

    @Mock
    private ClasseClient claseClient;

    @InjectMocks
    private ReservationServiceImpl service;

    private Reservation reservationEntity;
    private ReservationRequestDto requestDto;
    private UserResponseDto mockUser;
    private CoachResponseDto mockCoach;
    private ClasseResponseDto mockClasse;
    private LocalDateTime reservationDate;

    @BeforeEach
    void setUp() {
        reservationDate = LocalDateTime.of(2026, 6, 25, 14, 0);

        reservationEntity = new Reservation();
        reservationEntity.setIdReservation(1L);
        reservationEntity.setIdUser(10L);
        reservationEntity.setIdClasse(5L);
        reservationEntity.setIdCoach(3L);
        reservationEntity.setReservationDate(reservationDate);
        reservationEntity.setReservationStatus("CONFIRMADA");

        requestDto = new ReservationRequestDto();
        requestDto.setIdUser(10L);
        requestDto.setIdClasse(5L);
        requestDto.setIdCoach(3L);
        requestDto.setReservationDate(reservationDate);
        requestDto.setReservationStatus("CONFIRMADA");

        mockUser = new UserResponseDto();
        mockUser.setId(10L);
        mockUser.setUsername("felipe.perez");

        mockCoach = new CoachResponseDto();
        mockCoach.setIdCoach(3L);
        mockCoach.setName("Marcelo");

        mockClasse = new ClasseResponseDto();
        mockClasse.setId(5L);
    }

    // ==========================================
    // PRUEBAS: findAll() y findById() (Enriquecimiento Feliz y Fallido)
    // ==========================================
    @Test
    void givenExistingReservations_whenFindAll_thenReturnEnrichedList() {
        when(repository.findAll()).thenReturn(List.of(reservationEntity));
        when(userClient.findById(10L)).thenReturn(mockUser);
        when(coachClient.findById(3L)).thenReturn(mockCoach);
        when(claseClient.findById(5L)).thenReturn(mockClasse);

        List<ReservationResponseDto> result = service.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertNotNull(result.get(0).getUser());
        assertNotNull(result.get(0).getCoach());
        assertNotNull(result.get(0).getClasse());
    }

    @Test
    void givenFeignClientsException_whenFindById_thenReturnDtoWithNulls() {
        when(repository.findById(1L)).thenReturn(Optional.of(reservationEntity));
        // Forzamos los bloques catch del mapeador simulando caídas en los 3 servicios remotos
        when(userClient.findById(10L)).thenThrow(new RuntimeException("Error User"));
        when(coachClient.findById(3L)).thenThrow(new RuntimeException("Error Coach"));
        when(claseClient.findById(5L)).thenThrow(new RuntimeException("Error Classe"));

        ReservationResponseDto result = service.findById(1L);

        assertNotNull(result);
        assertNull(result.getUser());
        assertNull(result.getCoach());
        assertNull(result.getClasse());
    }

    @Test
    void givenNonExistingId_whenFindById_thenReturnNull() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        ReservationResponseDto result = service.findById(99L);
        assertNull(result);
    }

    // ==========================================
    // PRUEBAS: create() y update()
    // ==========================================
    @Test
    void givenValidRequest_whenCreate_thenReturnCreatedDto() {
        when(repository.save(any(Reservation.class))).thenReturn(reservationEntity);

        ReservationResponseDto result = service.create(requestDto);

        assertNotNull(result);
        assertEquals(1L, result.getIdReservation());
        verify(repository, times(1)).save(any(Reservation.class));
    }

    @Test
    void givenExistingId_whenUpdate_thenReturnUpdatedDto() {
        when(repository.existsById(1L)).thenReturn(true);
        when(repository.save(any(Reservation.class))).thenReturn(reservationEntity);

        ReservationResponseDto result = service.update(1L, requestDto);

        assertNotNull(result);
        verify(repository, times(1)).save(any(Reservation.class));
    }

    @Test
    void givenNonExistingId_whenUpdate_thenReturnNull() {
        when(repository.existsById(99L)).thenReturn(false);

        ReservationResponseDto result = service.update(99L, requestDto);

        assertNull(result);
        verify(repository, never()).save(any(Reservation.class));
    }

    // ==========================================
    // PRUEBAS: delete() (Cubre logs y alertas de error)
    // ==========================================
    @Test
    void givenExistingId_whenDelete_thenReturnTrue() {
        when(repository.existsById(1L)).thenReturn(true);
        doNothing().when(repository).deleteById(1L);

        boolean result = service.delete(1L);

        assertTrue(result);
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void givenNonExistingId_whenDelete_thenReturnFalse() {
        when(repository.existsById(99L)).thenReturn(false);

        boolean result = service.delete(99L);

        assertFalse(result);
        verify(repository, never()).deleteById(anyLong());
    }

    // ==========================================
    // PRUEBAS: findByUserId() (Cubre try-catch y nulls)
    // ==========================================
    @Test
    void givenNonExistingUserInFeign_whenFindByUserId_thenReturnNull() throws Exception {
        when(userClient.findById(99L)).thenReturn(null);

        List<ReservationResponseDto> result = service.findByUserId(99L);

        assertNull(result);
    }

    @Test
    void givenUserFeignException_whenFindByUserId_throwException() {
        when(userClient.findById(10L)).thenThrow(new RuntimeException("Timeout"));

        assertThrows(Exception.class, () -> service.findByUserId(10L));
    }

    // ==========================================
    // PRUEBAS: findByCoachId() (Cubre try-catch y nulls)
    // ==========================================
    @Test
    void givenNonExistingCoachInFeign_whenFindByCoachId_thenReturnNull() throws Exception {
        when(coachClient.findById(99L)).thenReturn(null);

        List<ReservationResponseDto> result = service.findByCoachId(99L);

        assertNull(result);
    }

    @Test
    void givenCoachFeignException_whenFindByCoachId_throwException() {
        when(coachClient.findById(3L)).thenThrow(new RuntimeException("Timeout"));

        assertThrows(Exception.class, () -> service.findByCoachId(3L));
    }

    // ==========================================
    // PRUEBAS: findByClasseId() (Cubre bloques truncados)
    // ==========================================
    @Test
    void givenNonExistingClasseInFeign_whenFindByClasseId_thenReturnNull() throws Exception {
        when(claseClient.findById(99L)).thenReturn(null);

        List<ReservationResponseDto> result = service.findByClasseId(99L);

        assertNull(result);
    }
}
