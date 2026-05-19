package cl.duoc.ms_reservas.repository;
import cl.duoc.ms_reservas.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservaRespository extends JpaRepository<Reserva,Long> {
    List<Reserva> findByUserId(Long UserId);
    List<Reserva>findByEntrenadorId(Long EntrenadorId);
    List<Reserva>findByClasseId(Long ClasseId);
}
