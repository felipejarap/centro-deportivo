package cl.duoc.ms_subscription.service.api;

import cl.duoc.ms_subscription.dto.PlansResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-plans")
public interface PlansClient {
    @GetMapping("/api/v1/plans/{id}")
    PlansResponseDto findById(@PathVariable Long id);
}
