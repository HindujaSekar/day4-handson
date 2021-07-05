package com.training.springbootusecase.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.training.springbootusecase.dto.AccountInfoDto;
import com.training.springbootusecase.dto.BeneficiaryDto;
import com.training.springbootusecase.dto.CredentialDto;
import com.training.springbootusecase.dto.FundTransferDto;
import com.training.springbootusecase.dto.LoginDto;
import com.training.springbootusecase.dto.RegisterDto;
import com.training.springbootusecase.dto.RequestDto;
import com.training.springbootusecase.service.AccountService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("fund-transfer")
@Slf4j
public class AccountController {
	
	@Autowired
	private AccountService service;
	
	@PostMapping("/{register}")
	public ResponseEntity<CredentialDto> register(@RequestBody RegisterDto registerDto){
		CredentialDto dto = service.addUser(registerDto);
		log.info("User with name " +registerDto.getName()+" is registered");
		return new ResponseEntity<>(dto,HttpStatus.OK);
	}
	@PostMapping("/{request}")
	public ResponseEntity<BeneficiaryDto> addBeneficary(@RequestBody RequestDto request){
		BeneficiaryDto dto = service.addBeneficiary(request);
		log.info("Beneficiary account added for " + dto.getAccountId());
		return new ResponseEntity<>(dto,HttpStatus.CREATED);
	}
	@PostMapping("/{fundtransfer}")
	public ResponseEntity<String> fundTransfer(@RequestBody FundTransferDto fundTransferDto){
		String message = service.fundTransfer(fundTransferDto);
		log.info("Your account is debited with " + fundTransferDto.getAmount());
		return new ResponseEntity<>(message,HttpStatus.OK);
	}
	@PostMapping("/{login}")
	public ResponseEntity<AccountInfoDto> login(@RequestBody LoginDto loginDto){
		
		AccountInfoDto dto = service.login(loginDto);
		log.info("logged in");
		return new ResponseEntity<>(dto,HttpStatus.OK);
		
	}

}
