package duoc.cl.ms_medicalRecord.service;

import duoc.cl.ms_medicalRecord.dto.MedicalRecordRequestDto;
import duoc.cl.ms_medicalRecord.dto.MedicalRecordResponseDto;

import java.util.List;

public interface MedicalRecordService {
    List<MedicalRecordResponseDto> findAll();
    MedicalRecordResponseDto findById(Long id);
    List<MedicalRecordResponseDto> findByUserId(Long userId) throws Exception;
    MedicalRecordResponseDto create(MedicalRecordRequestDto medicalRecord);
    MedicalRecordResponseDto update(Long id, MedicalRecordRequestDto medicalRecord);
    boolean deleteById(Long id);
}
