package cl.duoc.ms_rutinaEjercicio.service.api;

import cl.duoc.ms_rutinaEjercicio.dto.UserResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-users", url = "http://localhost:8081/api/v1/users")
public interface UserClient {

    @GetMapping("/{id}")
    UserResponseDto findById(@PathVariable Long id);
}
