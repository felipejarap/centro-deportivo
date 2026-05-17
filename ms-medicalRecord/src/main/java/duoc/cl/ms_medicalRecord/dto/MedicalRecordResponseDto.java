package duoc.cl.ms_medicalRecord.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Setter @Getter @NoArgsConstructor @AllArgsConstructor
public class MedicalRecordResponseDto {

    Long id;
    String allergy;
    String disease;
    String medicalCenter;
    Long userId;
}
