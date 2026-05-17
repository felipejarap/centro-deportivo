package duoc.cl.ms_medicalRecord.service.impl;

import duoc.cl.ms_medicalRecord.dto.MedicalRecordRequestDto;
import duoc.cl.ms_medicalRecord.dto.MedicalRecordResponseDto;
import duoc.cl.ms_medicalRecord.dto.UserResponseDto;
import duoc.cl.ms_medicalRecord.model.MedicalRecord;
import duoc.cl.ms_medicalRecord.repository.MedicalRecordRepository;
import duoc.cl.ms_medicalRecord.service.MedicalRecordService;
import duoc.cl.ms_medicalRecord.service.api.UserClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@AllArgsConstructor
public class MedicalRecordServiceImpl implements MedicalRecordService {
    private final MedicalRecordRepository repository;
    private final UserClient user;

    private MedicalRecordResponseDto toDto(MedicalRecord entity) {
        return new MedicalRecordResponseDto(
                entity.getId(),
                entity.getAllergy(),
                entity.getDisease(),
                entity.getMedicalCenter(),
                entity.getUserId()

        );
    }



    private MedicalRecord toEntity(MedicalRecordResponseDto dto) {
        return new MedicalRecord(
                dto.getId(),
                dto.getAllergy(),
                dto.getDisease(),
                dto.getMedicalCenter(),
                dto.getUserId()
        );
    }


    private MedicalRecord toEntity(MedicalRecordRequestDto dto) {
        return new MedicalRecord(
                dto.getId(),
                dto.getAllergy(),
                dto.getDisease(),
                dto.getMedicalCenter(),
                dto.getUserId()
        );
    }


    @Override
    public List<MedicalRecordResponseDto> findAll() {
        return repository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public MedicalRecordResponseDto findById(Long id) {
        return repository.findById(id).map(this::toDto).orElse(null);
    }

    @Override
    public List<MedicalRecordResponseDto> findByUserId(Long userId) throws Exception{
        try {
            UserResponseDto userFind = user.findById(userId);

            if (userFind == null) {
                return null;
            }
            return repository.findByUserId(userId).stream().map(this::toDto).toList();

        } catch (Exception e) {

            throw new Exception(e.getMessage());
        }
    }

    @Override
    public MedicalRecordResponseDto create(MedicalRecordRequestDto medicalRecord) {
        return toDto(repository.save(toEntity(medicalRecord)));
    }

    @Override
    public MedicalRecordResponseDto update(Long id, MedicalRecordRequestDto medicalRecord) {
        if (repository.existsById(id)) {
            MedicalRecord entity = toEntity(medicalRecord);
            entity.setId(id);
            return toDto(repository.save(entity));
        }
        return null;
    }

    @Override
    public boolean deleteById(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
