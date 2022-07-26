package com.finProyecto.fimoteca.jwt.persistencia.user;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.finProyecto.fimoteca.jwt.utils.Constantes;

@Repository
public interface IUserRepository extends CrudRepository<User, Long> {

	Optional<User> findUserByUsername(String username);

	@Query(value = "select u from User u WHERE u.username = :name and u.password = :password")
	Optional<User> buscarPorNombreUsuarioYPassword(@Param("name") String username, @Param("password") String password);
	
	
	
	@Transactional
	@Modifying
	@Query(value = "update " + Constantes.TABLA_USERS + " set last_login = :fechaLogin where username = :username", nativeQuery = true)
	void actualizarFechaLoginPorUsuario(@Param("username") String username, @Param("fechaLogin") LocalDateTime fechaLogin);
}
