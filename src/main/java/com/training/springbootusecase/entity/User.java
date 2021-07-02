package com.training.springbootusecase.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.ToString;

@Entity
@Table
@Data
@ToString
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long userId;
	@NotNull(message = "name is required")
	@Size(min=3, max=20,message="name should contain atleast 3 letters")
	private String name;
	@Email
	@NotNull(message = "email is required")
	private String email;
	@NotNull(message = "password is required")
	@Size(min =4)
	private String password;
	
	

}
