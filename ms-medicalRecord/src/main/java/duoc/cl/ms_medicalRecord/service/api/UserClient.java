package duoc.cl.ms_medicalRecord.service.api;


import duoc.cl.ms_medicalRecord.dto.UserResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "http://localhost:8080/api/v1/Users")
public interface UserClient {

    @GetMapping("/{id}")
    UserResponseDto findById(@PathVariable Long id);
}
