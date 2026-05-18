package cl.duoc.ms_subscription.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter @Getter @AllArgsConstructor @NoArgsConstructor
public class SubscriptionRequestDto {
    Long id;
    @NotNull
    Long userId;
    @NotNull
    Long plansId;
    LocalDateTime endDate;
    boolean state;
}
