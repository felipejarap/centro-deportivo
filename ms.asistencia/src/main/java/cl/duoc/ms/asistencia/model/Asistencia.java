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

public class Asistencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_asistencia")
    private Long idAsistencia;

    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;

    @Column(name = "id_classe", nullable = false)
    private Long idClasse;

    @Column(name = "hora_llegada", nullable = false)
    private String horaLlegada;

    @Column(name = "asistio", nullable = false)
    private Boolean asistio;
}
