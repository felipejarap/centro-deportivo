package cl.duoc.ms_reservas.service.api;


import cl.duoc.ms_reservas.dto.CoachResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-coaches")
public interface CoachClient {
    @GetMapping("/api/v1/coaches/{id}")
    CoachResponseDto findById(@PathVariable Long id);
}
