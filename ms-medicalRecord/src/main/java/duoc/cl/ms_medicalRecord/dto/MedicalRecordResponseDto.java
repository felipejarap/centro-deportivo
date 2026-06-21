package duoc.cl.ms_medicalRecord.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Setter @Getter @NoArgsConstructor @AllArgsConstructor
@Schema(description = "Modelo de respuesta que representa los datos públicos de la ficha médica de un usuario")
public class MedicalRecordResponseDto {

    @Schema(description = "Identificador único de la ficha médica registrado en la base de datos", example = "1")
    Long id;
    @Schema(description = "Detalle de las alergias registradas del usuario", example = "Penicilina, Polen")
    String allergy;
    @Schema(description = "Enfermedades crónicas o preexistencias médicas declaradas", example = "Asma leve")
    String disease;
    @Schema(description = "Nombre del centro médico emisor de la ficha", example = "Clínica Bupa Santiago")
    String medicalCenter;
    @Schema(description = "Identificador único del usuario dueño de esta ficha médica", example = "45")
    Long userId;
}
