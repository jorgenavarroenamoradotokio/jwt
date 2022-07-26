package com.finProyecto.fimoteca.jwt.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDTO {
	
	private long film;
	private long user;
	private String title;
	private String textReview;
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date date;
}
