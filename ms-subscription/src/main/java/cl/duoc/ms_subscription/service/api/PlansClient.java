package cl.duoc.ms_subscription.service.api;

import cl.duoc.ms_subscription.dto.PlansResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "plans-service", url = "http://localhost:8083/api/v1/plans")
public interface PlansClient {
    @GetMapping("/{id}")
    PlansResponseDto findById(@PathVariable Long id);
}
