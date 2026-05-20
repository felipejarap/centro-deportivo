package cl.duoc.ms_rutinaEjercicio.respository;

import cl.duoc.ms_rutinaEjercicio.model.RutinaEjercicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RutinaEjercicioRepository extends JpaRepository <RutinaEjercicio, Long> {

    List<RutinaEjercicio> findByIdUsuario(Long idUsuario);
    List<RutinaEjercicio> findByIdEntrenador(Long idEntrenador);
    List<RutinaEjercicio> findByObjetivo(String objetivo);
    List<RutinaEjercicio> findByIdUsuarioAndActiva(Long idUsuario, Boolean activa);
}
