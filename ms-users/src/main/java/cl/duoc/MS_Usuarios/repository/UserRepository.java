package cl.duoc.MS_Usuarios.repository;

import cl.duoc.MS_Usuarios.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.typeUser")
    List<User> findAllWithTypeUser();

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.typeUser WHERE u.idUser = :id")
    Optional<User> findByIdWithTypeUser(@Param("id") Long id);

    List<User> findByTypeUser_Id(Long typeUserId);
}
