package cl.duoc.ms_subscription.service.api;

import cl.duoc.ms_subscription.dto.UserResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-users")
public interface UserClient {
    @GetMapping("/api/v1/users/{id}")
    UserResponseDto findById(@PathVariable Long id);
}
