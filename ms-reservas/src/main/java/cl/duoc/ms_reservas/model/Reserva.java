package cl.duoc.ms_reservas.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reserva")
    private Long idReserva;

    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;

    @Column(name = "id_clase", nullable = false)
    private Long idClase;

    @Column(name = "id_entrenador", nullable = false)
    private Long idEntrenador;

    @Column(name = "fecha_reserva", nullable = false)
    private String fechaReserva;

    @Column(name = "estado_reserva", nullable = false)
    private String estadoReserva;



}
