package cl.duoc.ms_reservas.repository;
import cl.duoc.ms_reservas.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservaRespository extends JpaRepository<Reserva,Long> {
}
