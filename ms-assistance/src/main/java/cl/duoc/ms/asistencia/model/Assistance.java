package cl.duoc.ms.asistencia.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Assistance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_assistance")
    private Long idAssistance;

    @Column(name = "id_user", nullable = false)
    private Long idUser;

    @Column(name = "id_classe", nullable = false)
    private Long idClasse;

    @Column(name = "arrival_time", nullable = false)
    private String arrivalTime;

    @Column(name = "assist", nullable = false)
    private Boolean assist;
}
