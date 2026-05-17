package duoc.cl.ms_medicalRecord.repository;

import duoc.cl.ms_medicalRecord.model.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {
    List<MedicalRecord> findByUserId(Long userId);
}
