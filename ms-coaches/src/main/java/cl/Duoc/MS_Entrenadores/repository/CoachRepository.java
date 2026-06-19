package cl.Duoc.MS_Entrenadores.repository;

import cl.Duoc.MS_Entrenadores.model.Coach;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoachRepository extends JpaRepository<Coach,Long> {
}
