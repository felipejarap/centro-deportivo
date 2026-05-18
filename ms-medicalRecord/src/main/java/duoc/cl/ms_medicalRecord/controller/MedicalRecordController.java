package duoc.cl.ms_medicalRecord.controller;

import duoc.cl.ms_medicalRecord.dto.MedicalRecordRequestDto;
import duoc.cl.ms_medicalRecord.dto.MedicalRecordResponseDto;
import duoc.cl.ms_medicalRecord.service.MedicalRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/medical-records")
@RequiredArgsConstructor
public class MedicalRecordController {
    private final MedicalRecordService service;

    @GetMapping
    public ResponseEntity<List<MedicalRecordResponseDto>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicalRecordResponseDto> findById(@PathVariable Long id) {
        MedicalRecordResponseDto medicalRecord = service.findById(id);

        if (medicalRecord == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(medicalRecord);
    }

    @GetMapping("/by-user/{userId}")
    public ResponseEntity<List<MedicalRecordResponseDto>> findByUserId(@PathVariable Long userId) throws Exception {

        try {
            return ResponseEntity.ok(service.findByUserId(userId));

        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<MedicalRecordResponseDto> create(@Valid @RequestBody MedicalRecordRequestDto medicalRecord) {
        MedicalRecordResponseDto addMedicalRecord = service.create(medicalRecord);
        return ResponseEntity.status(HttpStatus.CREATED).body(addMedicalRecord);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicalRecordResponseDto> update(@PathVariable Long id, @Valid @RequestBody MedicalRecordRequestDto medicalRecord){
        MedicalRecordResponseDto updatedMedicalRecord = service.update(id, medicalRecord);
        if(updatedMedicalRecord == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedMedicalRecord);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        boolean deleted = service.deleteById(id);
        if(deleted){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }


}
