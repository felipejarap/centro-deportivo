package cl.duoc.ms_subscription.repository;

import cl.duoc.ms_subscription.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findByUserId(Long userId);
    List<Subscription> findByPlansId(Long plansId);
}
