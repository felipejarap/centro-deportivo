package cl.duoc.ms_reservas.service.api;

import cl.duoc.ms_reservas.dto.ClasseResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-classes")
public interface ClasseClient {
    @GetMapping("/api/v1/classes/{id}")
    ClasseResponseDto findById(@PathVariable Long id);


}
