package cl.duoc.ms_subscription.service.impl;

import cl.duoc.ms_subscription.dto.PlansResponseDto;
import cl.duoc.ms_subscription.dto.SubscriptionRequestDto;
import cl.duoc.ms_subscription.dto.SubscriptionResponseDto;
import cl.duoc.ms_subscription.dto.UserResponseDto;
import cl.duoc.ms_subscription.model.Subscription;
import cl.duoc.ms_subscription.repository.SubscriptionRepository;
import cl.duoc.ms_subscription.service.SubscriptionService;
import cl.duoc.ms_subscription.service.api.PlansClient;
import cl.duoc.ms_subscription.service.api.UserClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {
    private final SubscriptionRepository repository;
    private final UserClient userClient;
    private final PlansClient plansClient;

    private SubscriptionResponseDto toDto(Subscription entity) {
        return new SubscriptionResponseDto(
                entity.getId(),
                entity.getUserId(),
                entity.getPlansId(),
                entity.getEndDate(),
                entity.isState()

        );
    }



    private Subscription toEntity(SubscriptionResponseDto dto) {
        return new Subscription(
                dto.getId(),
                dto.getUserId(),
                dto.getPlansId(),
                dto.getEndDate(),
                dto.isState()
        );
    }


    private Subscription toEntity(SubscriptionRequestDto dto) {
        return new Subscription(
                dto.getId(),
                dto.getUserId(),
                dto.getPlansId(),
                dto.getEndDate(),
                dto.isState()
        );
    }

    @Override
    public List<SubscriptionResponseDto> findAll() {
        return repository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public SubscriptionResponseDto findById(Long id) {
        return repository.findById(id).map(this::toDto).orElse(null);
    }

    @Override
    public List<SubscriptionResponseDto> findByUserId(Long userId) throws Exception {
        try {
            UserResponseDto userFind = userClient.findById(userId);

            if (userFind == null) {
                return null;
            }
            return repository.findByUserId(userId).stream().map(this::toDto).toList();

        } catch (Exception e) {

            throw new Exception(e.getMessage());
        }
    }

    @Override
    public List<SubscriptionResponseDto> findByPlansId(Long plansId) throws Exception {
        try {
            PlansResponseDto plansFind = plansClient.findById(plansId);

            if (plansFind == null) {
                return null;
            }
            return repository.findByPlansId(plansId).stream().map(this::toDto).toList();

        } catch (Exception e) {

            throw new Exception(e.getMessage());
        }
    }

    @Override
    public SubscriptionResponseDto create(SubscriptionRequestDto subscription) {
        return toDto(repository.save(toEntity(subscription)));
    }

    @Override
    public SubscriptionResponseDto update(Long id, SubscriptionRequestDto subscription) {
        if (repository.existsById(id)) {
            Subscription entity = toEntity(subscription);
            entity.setId(id);
            return toDto(repository.save(entity));
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
