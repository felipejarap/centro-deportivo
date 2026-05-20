package cl.duoc.ms.asistencia.repository;

import cl.duoc.ms.asistencia.model.Asistencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {
    List<Asistencia> findByIdUsuario(Long idUsuario);
    List<Asistencia> findByIdClasse(Long idClasse);
}
