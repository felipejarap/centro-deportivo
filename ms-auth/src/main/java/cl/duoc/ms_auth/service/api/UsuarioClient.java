package cl.duoc.ms_auth.service.api;

import cl.duoc.ms_auth.dto.UsuarioResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-users")
public interface UsuarioClient {
    @GetMapping("/api/v1/users/{id}")
    UsuarioResponseDto findById(@PathVariable Long id);
}
