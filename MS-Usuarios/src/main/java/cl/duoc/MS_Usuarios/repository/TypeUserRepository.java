package cl.duoc.MS_Usuarios.repository;

import cl.duoc.MS_Usuarios.model.TypeUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TypeUserRepository extends JpaRepository<TypeUser, Long> {

    Optional<TypeUser> findByNameIgnoreCase(String name);
}
