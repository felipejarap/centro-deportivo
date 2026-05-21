package duoc.cl.ms_medicalRecord.controller;

import duoc.cl.ms_medicalRecord.dto.MedicalRecordRequestDto;
import duoc.cl.ms_medicalRecord.dto.MedicalRecordResponseDto;
import duoc.cl.ms_medicalRecord.service.MedicalRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/medical-records")
@RequiredArgsConstructor
public class MedicalRecordController {

    private static final Logger log = LoggerFactory.getLogger(MedicalRecordController.class);

    private final MedicalRecordService service;

    @GetMapping
    public ResponseEntity<List<MedicalRecordResponseDto>> findAll() {
        log.info("GET /api/v1/medical-records - Listando todos los registros médicos");
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicalRecordResponseDto> findById(@PathVariable Long id) {
        log.info("GET /api/v1/medical-records/{} - Buscando registro médico por id", id);
        MedicalRecordResponseDto medicalRecord = service.findById(id);
        if (medicalRecord == null) {
            log.warn("GET /api/v1/medical-records/{} - No encontrado, respondiendo 404", id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(medicalRecord);
    }

    @GetMapping("/by-user/{userId}")
    public ResponseEntity<List<MedicalRecordResponseDto>> findByUserId(@PathVariable Long userId) throws Exception {
        log.info("GET /api/v1/medical-records/by-user/{} - Buscando registros médicos por usuario", userId);
        try {
            List<MedicalRecordResponseDto> result = service.findByUserId(userId);
            log.info("GET /api/v1/medical-records/by-user/{} - Resultado: {} registros", userId,
                    result != null ? result.size() : 0);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.warn("GET /api/v1/medical-records/by-user/{} - Usuario no encontrado, respondiendo 404: {}",
                    userId, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<MedicalRecordResponseDto> create(@Valid @RequestBody MedicalRecordRequestDto medicalRecord) {
        log.info("POST /api/v1/medical-records - Solicitud de creación: userId={}", medicalRecord.getUserId());
        MedicalRecordResponseDto addMedicalRecord = service.create(medicalRecord);
        log.info("POST /api/v1/medical-records - Registro médico creado con id={}", addMedicalRecord.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(addMedicalRecord);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicalRecordResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody MedicalRecordRequestDto medicalRecord) {
        log.info("PUT /api/v1/medical-records/{} - Solicitud de actualización: userId={}", id, medicalRecord.getUserId());
        MedicalRecordResponseDto updatedMedicalRecord = service.update(id, medicalRecord);
        if (updatedMedicalRecord == null) {
            log.warn("PUT /api/v1/medical-records/{} - No encontrado para actualizar, respondiendo 404", id);
            return ResponseEntity.notFound().build();
        }
        log.info("PUT /api/v1/medical-records/{} - Actualización exitosa", id);
        return ResponseEntity.ok(updatedMedicalRecord);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/medical-records/{} - Solicitud de eliminación", id);
        boolean deleted = service.deleteById(id);
        if (deleted) {
            log.info("DELETE /api/v1/medical-records/{} - Eliminado exitosamente", id);
            return ResponseEntity.noContent().build();
        }
        log.warn("DELETE /api/v1/medical-records/{} - No encontrado para eliminar, respondiendo 404", id);
        return ResponseEntity.notFound().build();
    }
}