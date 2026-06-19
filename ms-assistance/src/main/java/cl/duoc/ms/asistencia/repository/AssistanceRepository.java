package cl.duoc.ms.asistencia.repository;

import cl.duoc.ms.asistencia.model.Assistance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssistanceRepository extends JpaRepository<Assistance, Long> {
    List<Assistance> findByIdUser(Long idUser);
    List<Assistance> findByIdClasse(Long idClasse);
}
