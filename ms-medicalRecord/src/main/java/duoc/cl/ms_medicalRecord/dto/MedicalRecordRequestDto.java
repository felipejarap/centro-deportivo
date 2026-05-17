package duoc.cl.ms_medicalRecord.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter @NoArgsConstructor @AllArgsConstructor
public class MedicalRecordRequestDto {
    Long id;
    @NotBlank(message = "El campo allergy no debe estar en blanco")
    String allergy;
    @NotBlank(message = "El disease no debe estar en blanco")
    String disease;
    @NotBlank(message = "El medical center no debe estar en blanco")
    String medicalCenter;
    @NotNull(message = "El user no debe ser nulo")
    Long userId;
}
