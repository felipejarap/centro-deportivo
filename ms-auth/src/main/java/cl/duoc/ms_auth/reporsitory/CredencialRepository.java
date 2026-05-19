package cl.duoc.ms_auth.reporsitory;


import cl.duoc.ms_auth.model.Credencial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CredencialRepository extends JpaRepository<Credencial,Long> {
    Optional<Credencial> findByUsername(String username);
}
