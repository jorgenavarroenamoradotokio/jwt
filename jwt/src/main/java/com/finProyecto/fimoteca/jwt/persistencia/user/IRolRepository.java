package com.finProyecto.fimoteca.jwt.persistencia.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface IRolRepository extends CrudRepository<Role, Long> {

	Optional<Role> findByName(String name);

	@Query("select r.name from Role r inner join User u on r.id = u.rol.id and u.username = :username")
	Optional<String> obtenerRolAsociadoUsuario(@Param("username") String username);
}
