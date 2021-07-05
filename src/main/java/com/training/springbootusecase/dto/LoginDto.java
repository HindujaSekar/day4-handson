package com.training.springbootusecase.dto;


import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class LoginDto {
	private String email;
	private String password;

}
