package cl.duoc.ms_rutinaEjercicio.model;
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

public class RutinaEjercicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rutina")
    private Long idRutina;

    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;

    @Column(name = "id_entrenador", nullable = false)
    private Long idEntrenador;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "objetivo", nullable = false)
    private String objetivo; // hipertrofia, cardio, fuerza, flexibilidad, etc.

    @Column(name = "ejercicios", nullable = false, columnDefinition = "TEXT")
    private String ejercicios; // JSON string con lista de ejercicios

    @Column(name = "peso_registrado")
    private Double pesoRegistrado;

    @Column(name = "marca_personal")
    private Double marcaPersonal;

    @Column(name = "fecha_asignacion", nullable = false)
    private String fechaAsignacion;

    @Column(name = "activa", nullable = false)
    private Boolean activa;
}
