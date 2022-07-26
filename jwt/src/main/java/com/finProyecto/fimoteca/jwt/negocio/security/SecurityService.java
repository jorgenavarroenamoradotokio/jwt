package com.finProyecto.fimoteca.jwt.negocio.security;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.finProyecto.fimoteca.jwt.persistencia.user.IUserRepository;
import com.finProyecto.fimoteca.jwt.persistencia.user.Role;
import com.finProyecto.fimoteca.jwt.persistencia.user.User;

@Service
public class SecurityService implements UserDetailsService {

	@Autowired
	IUserRepository repository;
		
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = repository.findUserByUsername(username).orElseThrow(()-> new UsernameNotFoundException("Usuario/Contraseña incorrectos"));
		UserDetails userDetails = buildsUserForAuthentication(user, getUserAuthority(user.getRol()));
		return userDetails;
	}
	
	private Collection<GrantedAuthority> getUserAuthority(Role userRol) {
		Collection<GrantedAuthority> roles = new ArrayList<>();
		roles.add(new SimpleGrantedAuthority(userRol.getName()));
		return roles;
	}

	private UserDetails buildsUserForAuthentication(User user, Collection<GrantedAuthority> authorities) {
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				user.isActive(), true, true, true, authorities);
	}
}
