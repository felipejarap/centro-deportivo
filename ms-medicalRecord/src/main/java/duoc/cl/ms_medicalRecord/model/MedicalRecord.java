package duoc.cl.ms_medicalRecord.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter @Getter @AllArgsConstructor @NoArgsConstructor
public class MedicalRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String allergy;
    private String disease;
    private String medicalCenter;
    @Column(name = "user_id")
    private Long userId;
}
