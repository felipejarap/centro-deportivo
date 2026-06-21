package duoc.cl.ms_medicalRecord.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Modelo de entrada requerido para registrar o actualizar una ficha médica de usuario")

public class MedicalRecordRequestDto {
    @Schema(description = "Identificador único de la ficha médica (Solo requerido para actualizaciones)", example = "1")
    Long id;
    @NotBlank(message = "El campo allergy no debe estar en blanco")
    @Schema(description = "Detalle de alergias conocidas del usuario (separadas por comas si son varias o 'Ninguna')", example = "Penicilina, Polen")
    String allergy;
    @NotBlank(message = "El disease no debe estar en blanco")
    @Schema(description = "Enfermedades crónicas o preexistencias médicas del usuario (o 'Ninguna')", example = "Asma leve")
    String disease;
    @NotBlank(message = "El medical center no debe estar en blanco")
    @Schema(description = "Nombre del centro médico donde se atendió u obtuvo el certificado", example = "Clínica Bupa Santiago")
    String medicalCenter;
    @NotNull(message = "El user no debe ser nulo")
    @Schema(description = "Identificador único del usuario dueño de la ficha médica (Proveniente de ms-users)", example = "45")
    Long userId;
}
