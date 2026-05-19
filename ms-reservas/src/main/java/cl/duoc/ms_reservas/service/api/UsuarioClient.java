package cl.duoc.ms_reservas.service.api;


import cl.duoc.ms_reservas.dto.UserResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-usuarios", url = "http://localhost:8080/api/v1/Users")
public interface UsuarioClient {
    @GetMapping("/{id}")
    UserResponseDto findById(@PathVariable Long id);


}
