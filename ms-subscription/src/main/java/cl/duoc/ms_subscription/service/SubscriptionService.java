package cl.duoc.ms_subscription.service;

import cl.duoc.ms_subscription.dto.SubscriptionRequestDto;
import cl.duoc.ms_subscription.dto.SubscriptionResponseDto;

import java.util.List;

public interface SubscriptionService {
    List<SubscriptionResponseDto> findAll();
    SubscriptionResponseDto findById(Long id);
    List<SubscriptionResponseDto> findByUserId(Long userId) throws Exception;
    List<SubscriptionResponseDto> findByPlansId(Long plansId) throws Exception;
    SubscriptionResponseDto create(SubscriptionRequestDto subscription);
    SubscriptionResponseDto update(Long id, SubscriptionRequestDto subscription);
    boolean deleteById(Long id);
}
