package cl.duoc.ms_reservas.repository;
import cl.duoc.ms_reservas.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRespository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByIdUser(Long idUser);
    List<Reservation> findByIdCoach(Long idCoach);
    List<Reservation> findByIdClasse(Long idClasse);
}