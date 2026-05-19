package cl.duoc.ms_subscription.service.api;

import cl.duoc.ms_subscription.dto.UserResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "http://localhost:8081/api/v1/Users")
public interface UserClient {
    @GetMapping("/{id}")
    UserResponseDto findById(@PathVariable Long id);
}
