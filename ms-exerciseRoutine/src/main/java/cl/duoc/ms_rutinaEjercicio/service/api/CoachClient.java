package cl.duoc.ms_rutinaEjercicio.service.api;


import cl.duoc.ms_rutinaEjercicio.dto.CoachResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-coaches", url = "http://localhost:8085/api/v1/coaches")
public interface CoachClient {

    @GetMapping("/{id}")
    CoachResponseDto findById(@PathVariable Long id);
}
