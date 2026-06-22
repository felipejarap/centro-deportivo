package cl.duoc.ms_subscription.service.impl;

import cl.duoc.ms_subscription.dto.PlansResponseDto;
import cl.duoc.ms_subscription.dto.SubscriptionRequestDto;
import cl.duoc.ms_subscription.dto.SubscriptionResponseDto;
import cl.duoc.ms_subscription.dto.UserResponseDto;
import cl.duoc.ms_subscription.model.Subscription;
import cl.duoc.ms_subscription.repository.SubscriptionRepository;
import cl.duoc.ms_subscription.service.api.PlansClient;
import cl.duoc.ms_subscription.service.api.UserClient;
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
class SubscriptionServiceImplTest {

    @Mock
    private SubscriptionRepository repository;

    @Mock
    private UserClient userClient;

    @Mock
    private PlansClient plansClient;

    @InjectMocks
    private SubscriptionServiceImpl service;

    private Subscription subscriptionEntity;
    private SubscriptionRequestDto requestDto;
    private UserResponseDto mockUserResponse;
    private PlansResponseDto mockPlansResponse;
    private LocalDateTime endDate;

    @BeforeEach
    void setUp() {
        endDate = LocalDateTime.now().plusDays(30);

        subscriptionEntity = new Subscription(1L, 10L, 5L, endDate, true);
        requestDto = new SubscriptionRequestDto(1L, 10L, 5L, endDate, true);

        mockUserResponse = new UserResponseDto(); // Objeto vacío simulado
        mockPlansResponse = new PlansResponseDto(); // Objeto vacío simulado
    }


    // PRUEBAS: findAll() y findById()

    @Test
    void givenExistingSubscriptions_whenFindAll_thenReturnList() {
        when(repository.findAll()).thenReturn(List.of(subscriptionEntity));
        List<SubscriptionResponseDto> result = service.findAll();
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void givenExistingId_whenFindById_thenReturnDto() {
        when(repository.findById(1L)).thenReturn(Optional.of(subscriptionEntity));
        SubscriptionResponseDto result = service.findById(1L);
        assertNotNull(result);
        assertTrue(result.isState());
    }

    @Test
    void givenNonExistingId_whenFindById_thenReturnNull() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        SubscriptionResponseDto result = service.findById(99L);
        assertNull(result);
    }


    // PRUEBAS: findByUserId() (Feign UserClient)

    @Test
    void givenExistingUserInFeign_whenFindByUserId_thenReturnList() throws Exception {
        when(userClient.findById(10L)).thenReturn(mockUserResponse);
        when(repository.findByUserId(10L)).thenReturn(List.of(subscriptionEntity));

        List<SubscriptionResponseDto> result = service.findByUserId(10L);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(repository, times(1)).findByUserId(10L);
    }

    @Test
    void givenNonExistingUserInFeign_whenFindByUserId_thenReturnNull() throws Exception {
        when(userClient.findById(99L)).thenReturn(null);

        List<SubscriptionResponseDto> result = service.findByUserId(99L);

        assertNull(result);
        verify(repository, never()).findByUserId(anyLong());
    }

    @Test
    void givenUserFeignException_whenFindByUserId_throwException() {
        when(userClient.findById(10L)).thenThrow(new RuntimeException("Error de red"));

        Exception exception = assertThrows(Exception.class, () -> service.findByUserId(10L));
        assertTrue(exception.getMessage().contains("Error de red"));
    }


    // PRUEBAS: findByPlansId() (Feign PlansClient)

    @Test
    void givenExistingPlansInFeign_whenFindByPlansId_thenReturnList() throws Exception {
        when(plansClient.findById(5L)).thenReturn(mockPlansResponse);
        when(repository.findByPlansId(5L)).thenReturn(List.of(subscriptionEntity));

        List<SubscriptionResponseDto> result = service.findByPlansId(5L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void givenNonExistingPlansInFeign_whenFindByPlansId_thenReturnNull() throws Exception {
        when(plansClient.findById(99L)).thenReturn(null);

        List<SubscriptionResponseDto> result = service.findByPlansId(99L);

        assertNull(result);
        verify(repository, never()).findByPlansId(anyLong());
    }

    @Test
    void givenPlansFeignException_whenFindByPlansId_throwException() {
        when(plansClient.findById(5L)).thenThrow(new RuntimeException("Timeout Plan"));

        Exception exception = assertThrows(Exception.class, () -> service.findByPlansId(5L));
        assertTrue(exception.getMessage().contains("Timeout Plan"));
    }


    // PRUEBAS: create() y update()

    @Test
    void givenValidRequest_whenCreate_thenReturnCreatedDto() {
        when(repository.save(any(Subscription.class))).thenReturn(subscriptionEntity);
        SubscriptionResponseDto result = service.create(requestDto);
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void givenExistingId_whenUpdate_thenReturnUpdatedDto() {
        when(repository.existsById(1L)).thenReturn(true);
        when(repository.save(any(Subscription.class))).thenReturn(subscriptionEntity);

        SubscriptionResponseDto result = service.update(1L, requestDto);

        assertNotNull(result);
        verify(repository, times(1)).save(any(Subscription.class));
    }

    @Test
    void givenNonExistingId_whenUpdate_thenReturnNull() {
        when(repository.existsById(99L)).thenReturn(false);
        SubscriptionResponseDto result = service.update(99L, requestDto);
        assertNull(result);
        verify(repository, never()).save(any(Subscription.class));
    }


    // PRUEBAS: deleteById()
    @Test
    void givenExistingId_whenDeleteById_thenReturnTrue() {
        when(repository.existsById(1L)).thenReturn(true);
        doNothing().when(repository).deleteById(1L);

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
}
