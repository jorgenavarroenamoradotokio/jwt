package com.finProyecto.fimoteca.jwt.negocio.usuario;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.finProyecto.fimoteca.jwt.negocio.exception.NotFoundException;
import com.finProyecto.fimoteca.jwt.persistencia.user.IRolRepository;
import com.finProyecto.fimoteca.jwt.persistencia.user.IUserRepository;
import com.finProyecto.fimoteca.jwt.persistencia.user.Role;
import com.finProyecto.fimoteca.jwt.persistencia.user.User;

@SpringBootTest
@RunWith(JUnit4.class)
class TestGestorUsuarios {

	@Mock
	IRolRepository rolRepositoryMock;
	
	@Mock
	IUserRepository userRepositoryMock;
	
	
	@Test
	public void testAllRoles() {
		when(rolRepositoryMock.findAll()).thenReturn(listadoRoles());
		GestorUsuario gestorUsuario = new GestorUsuario();
		gestorUsuario.rolRepository = rolRepositoryMock;
		Set<Role> roles = gestorUsuario.buscarTodosRoles();
		assertNotNull(roles);
		assertEquals(roles.isEmpty(), false);		
	}
	
	@Test
	public void testAllRolesEmpty() {
		when(rolRepositoryMock.findAll()).thenReturn(new HashSet<Role>());
		GestorUsuario gestorUsuario = new GestorUsuario();
		gestorUsuario.rolRepository = rolRepositoryMock;
		Set<Role> roles = gestorUsuario.buscarTodosRoles();
		assertNotNull(roles);
		assertEquals(roles.isEmpty(), true);		
	}
	
	@Test
	public void testSearchRoleException() {
		when(rolRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.empty());
		GestorUsuario gestorUsuario = new GestorUsuario();
		gestorUsuario.rolRepository = rolRepositoryMock;
		NotFoundException thrown = assertThrows(NotFoundException.class, () -> {
			gestorUsuario.buscarRolPorId(5L);
		}, "Error");
		
		assertNotNull(thrown);
	}
	
	@Test
	public void testSearchRoleNoException() {
		try {
			Optional<Role> opt = Optional.of(listadoRoles().iterator().next());
			when(rolRepositoryMock.findById(Mockito.anyLong())).thenReturn(opt);
			GestorUsuario gestorUsuario = new GestorUsuario();
			gestorUsuario.rolRepository = rolRepositoryMock;
			Role result = gestorUsuario.buscarRolPorId(1L);
			assertNotNull(result);
			assertEquals(result.getId(), 1L);
		} catch (Exception ex) {
			fail(ex.getMessage());
		}		
	}
	
	
	@Test
	public void testSearchRoleNombreException() {
		when(rolRepositoryMock.findByName(Mockito.anyString())).thenReturn(Optional.ofNullable(null));
		GestorUsuario gestorUsuario = new GestorUsuario();
		gestorUsuario.rolRepository = rolRepositoryMock;
		NotFoundException thrown = assertThrows(NotFoundException.class, () -> {
			gestorUsuario.buscarRolPorNombre("asd");
		}, "Error");
		
		assertNotNull(thrown);
	}
	
	@Test
	public void testSearchRoleNombreNoException() {	
		try {
			Optional<Role> opt = Optional.of(listadoRoles().iterator().next());
			when(rolRepositoryMock.findByName(Mockito.anyString())).thenReturn(opt);
			GestorUsuario gestorUsuario = new GestorUsuario();
			gestorUsuario.rolRepository = rolRepositoryMock;
			Role result = gestorUsuario.buscarRolPorNombre("Test");
			assertNotNull(result);
			assertEquals(result.getName(), "Test");
		} catch (Exception ex) {
			fail(ex.getMessage());
		}
	}
	
	@Test
	public void testExistsUser() {
		Optional<User> opt = Optional.of(User.builder().id(1).username("Test").build());
		when(userRepositoryMock.findUserByUsername(Mockito.anyString())).thenReturn(opt);
		GestorUsuario gestorUsuario = new GestorUsuario();
		gestorUsuario.userRepository = userRepositoryMock;
		boolean result = gestorUsuario.existeUser("Test");
		assertEquals(result, true);
	}
	
	@Test
	public void testNoExistsUser() {
		when(userRepositoryMock.findUserByUsername(Mockito.anyString())).thenReturn(Optional.ofNullable(null));
		GestorUsuario gestorUsuario = new GestorUsuario();
		gestorUsuario.userRepository = userRepositoryMock;
		boolean result = gestorUsuario.existeUser("Test");
		assertEquals(result, false);
	}
	
	
	private HashSet<Role> listadoRoles(){
		HashSet<Role> roles = new HashSet<>();
		roles.add(Role.builder().id(1).name("Test").build());
		return roles;
	}
	
}