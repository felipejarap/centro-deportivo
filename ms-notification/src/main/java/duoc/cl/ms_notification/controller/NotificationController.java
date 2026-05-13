package duoc.cl.ms_notification.controller;

import duoc.cl.ms_notification.dto.NotificationRequestDto;
import duoc.cl.ms_notification.dto.NotificationResponseDto;
import duoc.cl.ms_notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService service;


    @GetMapping
    public ResponseEntity<List<NotificationResponseDto>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationResponseDto> findById(@PathVariable Long id) {
        NotificationResponseDto dto = service.findById(id);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<?>send(@RequestBody NotificationRequestDto dto)
    {
        try {
               service.send(dto);
            return ResponseEntity.ok("Notificación enviada correctamente a " + dto.getTo());
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error al procesar la notificación: " + e.getMessage());
        }
    }
}
