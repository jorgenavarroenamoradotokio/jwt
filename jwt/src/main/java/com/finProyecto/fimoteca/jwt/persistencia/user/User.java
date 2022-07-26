package com.finProyecto.fimoteca.jwt.persistencia.user;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.finProyecto.fimoteca.jwt.utils.Constantes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = Constantes.TABLA_USERS)
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(nullable = false)
	private String username;
	
	@Column(nullable = false)
	private String password;
	
	private String name;
	
	private String surname;
	
	@Column(nullable = false)
	private String email;
	
	@Column(name = "birth_Date")
	private Date birthDate;
	
	@Column(name = "creation_date")
	private LocalDate creationDate;
	
	@Column(name = "last_login")
	private LocalDateTime lastLogin;
	
	@Column(name = "active")
	private boolean active;
	
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_rol", nullable = false, foreignKey = @ForeignKey(name = "FK_ROL_ID",foreignKeyDefinition = "FOREIGN KEY (id_rol) REFERENCES roles(id) ON DELETE CASCADE"))
	private Role rol;
}