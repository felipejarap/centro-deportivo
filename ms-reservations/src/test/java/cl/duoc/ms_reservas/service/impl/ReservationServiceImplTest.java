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
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// Habilita el uso de Mockito en JUnit 5 para simular objetos sin levantar Spring
@ExtendWith(MockitoExtension.class)
// Evita que la prueba falle si configuras un simulador (stub) que finalmente no se llega a ejecutar
@MockitoSettings(strictness = Strictness.LENIENT)
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

    // =========================================================================
    // PRUEBAS: METODOS findAll() Y findById() (Enriquecimiento)
    // =========================================================================

    /**
     * CASO FELIZ: Evalúa que al buscar todas las reservas, el servicio llame correctamente
     * a la BD y luego "enriquezca" la información llamando en cadena a los 3 microservicios remotos.
     */
    @Test
    void givenExistingReservations_whenFindAll_thenReturnEnrichedList() {
        // GIVEN
        when(repository.findAll()).thenReturn(List.of(reservationEntity));
        when(userClient.findById(10L)).thenReturn(mockUser);
        when(coachClient.findById(3L)).thenReturn(mockCoach);
        when(claseClient.findById(5L)).thenReturn(mockClasse);

        // WHEN
        List<ReservationResponseDto> result = service.findAll();

        // THEN
        assertNotNull(result);
        assertEquals(1, result.size());
        assertNotNull(result.getFirst().getUser());
        assertNotNull(result.getFirst().getCoach());
        assertNotNull(result.getFirst().getClasse());
    }

    /**
     * CASO DE TOLERANCIA A FALLOS: Si la reserva existe en la BD pero los microservicios externos
     * fallan (lanzan excepción), el servicio debe capturar el error y devolver la reserva con datos nulos
     * en lugar de romper toda la pantalla o la aplicación.
     */
    @Test
    void givenFeignClientsException_whenFindById_thenReturnDtoWithNulls() {
        // GIVEN
        when(repository.findById(1L)).thenReturn(Optional.of(reservationEntity));
        when(userClient.findById(10L)).thenThrow(new RuntimeException("Error User"));
        when(coachClient.findById(3L)).thenThrow(new RuntimeException("Error Coach"));
        when(claseClient.findById(5L)).thenThrow(new RuntimeException("Error Classe"));

        // WHEN
        ReservationResponseDto result = service.findById(1L);

        // THEN
        assertNotNull(result);
        assertNull(result.getUser());
        assertNull(result.getCoach());
        assertNull(result.getClasse());
    }

    /**
     * CASO VACÍO: Si buscamos un ID de reserva que no existe en nuestra base de datos,
     * el servicio debe retornar directamente un valor nulo sin intentar llamar a los microservicios.
     */
    @Test
    void givenNonExistingId_whenFindById_thenReturnNull() {
        // GIVEN
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // WHEN
        ReservationResponseDto result = service.findById(99L);

        // THEN
        assertNull(result);
    }

    // =========================================================================
    // PRUEBAS: METODOS create() Y update()
    // =========================================================================

    /**
     * CREACIÓN EXITOSA: Comprueba que al enviar datos válidos, la entidad se mapea correctamente,
     * se guarda en el repositorio y nos devuelve el DTO con su ID generado.
     */
    @Test
    void givenValidRequest_whenCreate_thenReturnCreatedDto() {
        // GIVEN
        when(repository.save(any(Reservation.class))).thenReturn(reservationEntity);

        // WHEN
        ReservationResponseDto result = service.create(requestDto);

        // THEN
        assertNotNull(result);
        assertEquals(1L, result.getIdReservation());
        verify(repository, times(1)).save(any(Reservation.class));
    }

    /**
     * ACTUALIZACIÓN EXITOSA: Verifica el flujo donde el ID de la reserva sí existe,
     * permitiendo que el repositorio guarde los nuevos cambios sobreescribiendo el registro.
     */
    @Test
    void givenExistingId_whenUpdate_thenReturnUpdatedDto() {
        // GIVEN
        when(repository.existsById(1L)).thenReturn(true);
        when(repository.save(any(Reservation.class))).thenReturn(reservationEntity);

        // WHEN
        ReservationResponseDto result = service.update(1L, requestDto);

        // THEN
        assertNotNull(result);
        verify(repository, times(1)).save(any(Reservation.class));
    }

    /**
     * ACTUALIZACIÓN FALLIDA: Si intentamos actualizar una reserva con un ID inexistente,
     * el método debe retornar null de inmediato y PROHIBIR que el repositorio intente guardar algo.
     */
    @Test
    void givenNonExistingId_whenUpdate_thenReturnNull() {
        // GIVEN
        when(repository.existsById(99L)).thenReturn(false);

        // WHEN
        ReservationResponseDto result = service.update(99L, requestDto);

        // THEN
        assertNull(result);
        verify(repository, never()).save(any(Reservation.class));
    }

    // =========================================================================
    // PRUEBAS: METODO delete()
    // =========================================================================

    /**
     * ELIMINACIÓN EXITOSA: Si la reserva existe, se debe ejecutar el borrado en la base
     * de datos y retornar un indicador verdadero (true).
     */
    @Test
    void givenExistingId_whenDelete_thenReturnTrue() {
        // GIVEN
        when(repository.existsById(1L)).thenReturn(true);
        doNothing().when(repository).deleteById(1L);

        // WHEN
        boolean result = service.delete(1L);

        // THEN
        assertTrue(result);
        verify(repository, times(1)).deleteById(1L);
    }

    /**
     * ELIMINACIÓN FALLIDA: Si la reserva no existe, no se debe intentar borrar nada
     * en el repositorio y debe retornar falso (false).
     */
    @Test
    void givenNonExistingId_whenDelete_thenReturnFalse() {
        // GIVEN
        when(repository.existsById(99L)).thenReturn(false);

        // WHEN
        boolean result = service.delete(99L);

        // THEN
        assertFalse(result);
        verify(repository, never()).deleteById(99L);
    }

    // =========================================================================
    // PRUEBAS: METODO findByUserId() (Filtro por Microservicio de Usuarios)
    // =========================================================================

    /**
     * BUSQUEDA POR USUARIO NO EXISTENTE: Comprueba el comportamiento cuando el cliente Feign
     * responde que el usuario no existe en su sistema (devuelve null), retornando una lista vacía o null.
     */
    @Test
    void givenNonExistingUserInFeign_whenFindByUserId_thenReturnNull() throws Exception {
        // GIVEN
        when(userClient.findById(99L)).thenReturn(null);

        // WHEN
        List<ReservationResponseDto> result = service.findByUserId(99L);

        // THEN
        assertNull(result);
    }

    /**
     * CONTROL DE EXCEPCIONES: Si el microservicio de usuarios está caído o da Timeout,
     * evaluamos que nuestro servicio no esconda el error, sino que propague la excepción hacia arriba.
     */
    @Test
    void givenUserFeignException_whenFindByUserId_throwException() {
        // GIVEN: Simulamos el fallo de red o timeout
        when(coachClient.findById(3L)).thenThrow(new RuntimeException("Timeout"));

        // WHEN & THEN: Capturamos cualquier tipo de Exception que maneje tu servicio real
        assertThrows(Exception.class, () -> service.findByCoachId(3L));
    }

    // =========================================================================
    // PRUEBAS: METODO findByCoachId() (Filtro por Microservicio de Entrenadores)
    // =========================================================================

    /**
     * BUSQUEDA POR COACH INEXISTENTE: Asegura que si el cliente Feign retorna null al buscar
     * al entrenador, nuestro método de reservas se detenga y retorne null.
     */
    @Test
    void givenNonExistingCoachInFeign_whenFindByCoachId_thenReturnNull() throws Exception {
        // GIVEN
        when(coachClient.findById(99L)).thenReturn(null);

        // WHEN
        List<ReservationResponseDto> result = service.findByCoachId(99L);

        // THEN
        assertNull(result);
    }

    /**
     * CONTROL DE EXCEPCIONES: Asegura que los errores de comunicación de red con el servicio
     * de entrenadores escalen correctamente en el sistema.
     */
    @Test
    void givenCoachFeignException_whenFindByCoachId_throwException() {
        when(userClient.findById(10L)).thenThrow(new RuntimeException("Timeout"));
        // WHEN & THEN: Capturamos cualquier tipo de Exception que maneje tu servicio real
        assertThrows(Exception.class, () -> service.findByUserId(10L));
    }

    // =========================================================================
    // PRUEBAS: METODO findByClasseId() (Filtro por Microservicio de Clases)
    // =========================================================================

    /**
     * BUSQUEDA POR CLASE INEXISTENTE: Garantiza que si la clase consultada vía Feign no existe,
     * el servicio de reservas maneje la situación limpiamente respondiendo con un valor nulo.
     */
    @Test
    void givenNonExistingClasseInFeign_whenFindByClasseId_thenReturnNull() throws Exception {
        // GIVEN
        when(claseClient.findById(99L)).thenReturn(null);

        // WHEN
        List<ReservationResponseDto> result = service.findByClasseId(99L);

        // THEN
        assertNull(result);
    }
}
