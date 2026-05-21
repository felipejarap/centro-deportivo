package duoc.cl.ms_medicalRecord.service.impl;
import duoc.cl.ms_medicalRecord.dto.MedicalRecordRequestDto;
import duoc.cl.ms_medicalRecord.dto.MedicalRecordResponseDto;
import duoc.cl.ms_medicalRecord.dto.UserResponseDto;
import duoc.cl.ms_medicalRecord.model.MedicalRecord;
import duoc.cl.ms_medicalRecord.repository.MedicalRecordRepository;
import duoc.cl.ms_medicalRecord.service.MedicalRecordService;
import duoc.cl.ms_medicalRecord.service.api.UserClient;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MedicalRecordServiceImpl implements MedicalRecordService {

    private static final Logger log = LoggerFactory.getLogger(MedicalRecordServiceImpl.class);

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
        log.info("Consultando todos los registros médicos");
        List<MedicalRecordResponseDto> result = repository.findAll().stream().map(this::toDto).toList();
        log.info("Total de registros médicos encontrados: {}", result.size());
        return result;
    }

    @Override
    public MedicalRecordResponseDto findById(Long id) {
        log.info("Buscando registro médico por id: {}", id);
        return repository.findById(id)
                .map(entity -> {
                    log.info("Registro médico encontrado: id={}", id);
                    return toDto(entity);
                })
                .orElseGet(() -> {
                    log.warn("Registro médico no encontrado: id={}", id);
                    return null;
                });
    }

    @Override
    public List<MedicalRecordResponseDto> findByUserId(Long userId) throws Exception {
        log.info("Buscando registros médicos por usuario: userId={}", userId);
        try {
            UserResponseDto userFind = user.findById(userId);
            if (userFind == null) {
                log.warn("Usuario no encontrado en ms-usuarios: userId={}", userId);
                return null;
            }
            List<MedicalRecordResponseDto> result = repository.findByUserId(userId)
                    .stream().map(this::toDto).toList();
            log.info("Registros médicos encontrados para usuario: userId={}, total={}", userId, result.size());
            return result;
        } catch (Exception e) {
            log.error("Error al buscar registros médicos por usuario: userId={}, motivo={}", userId, e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public MedicalRecordResponseDto create(MedicalRecordRequestDto medicalRecord) {
        log.info("Creando registro médico: userId={}, allergy={}, disease={}, medicalCenter={}",
                medicalRecord.getUserId(), medicalRecord.getAllergy(),
                medicalRecord.getDisease(), medicalRecord.getMedicalCenter());
        MedicalRecordResponseDto saved = toDto(repository.save(toEntity(medicalRecord)));
        log.info("Registro médico creado exitosamente: id={}, userId={}", saved.getId(), saved.getUserId());
        return saved;
    }

    @Override
    public MedicalRecordResponseDto update(Long id, MedicalRecordRequestDto medicalRecord) {
        log.info("Actualizando registro médico: id={}, userId={}", id, medicalRecord.getUserId());
        if (repository.existsById(id)) {
            MedicalRecord entity = toEntity(medicalRecord);
            entity.setId(id);
            MedicalRecordResponseDto updated = toDto(repository.save(entity));
            log.info("Registro médico actualizado exitosamente: id={}", id);
            return updated;
        }
        log.warn("No se pudo actualizar: registro médico no encontrado: id={}", id);
        return null;
    }

    @Override
    public boolean deleteById(Long id) {
        log.info("Eliminando registro médico: id={}", id);
        if (repository.existsById(id)) {
            repository.deleteById(id);
            log.info("Registro médico eliminado exitosamente: id={}", id);
            return true;
        }
        log.warn("No se pudo eliminar: registro médico no encontrado: id={}", id);
        return false;
    }
}