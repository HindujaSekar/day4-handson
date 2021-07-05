package com.training.springbootusecase.dto;

import com.training.springbootusecase.entity.AccountType;
import com.training.springbootusecase.entity.GenderType;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class RegisterDto {
	
	private String name;
	private String email;
	private String password;
	private GenderType gender;
	private AccountType accountType;
	private double balance;

}
