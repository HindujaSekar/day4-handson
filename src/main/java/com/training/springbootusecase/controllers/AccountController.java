package com.training.springbootusecase.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.training.springbootusecase.dto.AccountInfoDto;
import com.training.springbootusecase.dto.BeneficiaryDto;
import com.training.springbootusecase.dto.CredentialDto;
import com.training.springbootusecase.dto.RequestDto;
import com.training.springbootusecase.entity.Account;
import com.training.springbootusecase.entity.User;
import com.training.springbootusecase.service.AccountService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("fund-transfer")
@Slf4j
public class AccountController {
	
	@Autowired
	private AccountService service;
	
	@PostMapping("/{user}/{balance}")
	public ResponseEntity<CredentialDto> register(@RequestBody User user,
			@PathVariable("balance") double balance){
		CredentialDto dto = service.addUser(user, balance);
		log.info("User with name" +user.getName()+"is registered");
		return new ResponseEntity<>(dto,HttpStatus.OK);
	}
	@PostMapping("/{request}")
	public ResponseEntity<BeneficiaryDto> addBeneficary(@RequestBody RequestDto request){
		BeneficiaryDto dto = service.addBeneficiary(request);
		log.info("Beneficiary account added for " + dto.getAccountId());
		return new ResponseEntity<>(dto,HttpStatus.CREATED);
	}
	@PutMapping("/{request}")
	public ResponseEntity<Account> changeBeneficiary(@RequestBody RequestDto request){
		Account account = service.changeBeneficiary(request);
		log.info("Beneficiary changed");
		return new ResponseEntity<>(account,HttpStatus.OK);
	}
	@PutMapping("/{id}/{amount}")
	public ResponseEntity<String> fundTransfer(@PathVariable("id") long accountId,
			@PathVariable("amount")double amount){
		String message = service.fundTransfer(accountId, amount);
		log.info("Your account is debited with " + amount);
		return new ResponseEntity<>(message,HttpStatus.OK);
	}
	@GetMapping("/{email}/{password}")
	public ResponseEntity<AccountInfoDto> login(@PathVariable("email") String email,
			@PathVariable("password") String password){
		
		AccountInfoDto dto = service.login(email, password);
		log.info("logged in");
		return new ResponseEntity<>(dto,HttpStatus.OK);
		
	}

}
