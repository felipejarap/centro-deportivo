package cl.duoc.ms_reservas.service.api;


import cl.duoc.ms_reservas.dto.EntrenadorResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-entrenadores", url = "http://localhost:8081/api/v1/entrenadores")
public interface EntrenadorClient {
    @GetMapping("/{id}")
    EntrenadorResponseDto findById(@PathVariable Long id);
}
