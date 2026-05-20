package cl.duoc.ms.asistencia.service.api;

import cl.duoc.ms.asistencia.dto.ClasseResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-classes", url = "http://localhost:8082/api/v1/classes")
public interface ClasseClient {
    @GetMapping("/{id}")
    ClasseResponseDto findById(@PathVariable Long id);
}
