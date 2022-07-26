package com.finProyecto.fimoteca.jwt.persistencia.review;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.finProyecto.fimoteca.jwt.persistencia.user.User;
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
@Table(name = Constantes.TABLA_COMENTARIOS)
public class Review {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String title;
	@Lob
	private String textReview;
	private Date date;

	@Column(nullable = false) 
	private long film;
	
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(name = "id_user", nullable = false, foreignKey = @ForeignKey(name = "FK_REVIEW_USER_ID", foreignKeyDefinition = "FOREIGN KEY (id_user) REFERENCES "
			+ Constantes.TABLA_USERS + "(id) ON DELETE CASCADE"))
	private User user;
}
