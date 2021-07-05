package com.training.springbootusecase.service;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.training.springbootusecase.dto.AccountInfoDto;
import com.training.springbootusecase.dto.BeneficiaryDto;
import com.training.springbootusecase.dto.CredentialDto;
import com.training.springbootusecase.dto.FundTransferDto;
import com.training.springbootusecase.dto.LoginDto;
import com.training.springbootusecase.dto.RegisterDto;
import com.training.springbootusecase.dto.RequestDto;
import com.training.springbootusecase.entity.Account;
import com.training.springbootusecase.entity.BeneficiaryConnections;
import com.training.springbootusecase.entity.TransactionHistory;
import com.training.springbootusecase.entity.User;
import com.training.springbootusecase.exceptions.AuthenticationException;
import com.training.springbootusecase.exceptions.DuplicateUserException;
import com.training.springbootusecase.exceptions.NoSuchAccountException;
import com.training.springbootusecase.exceptions.NotSufficientFundException;
import com.training.springbootusecase.repository.AccountRepository;
import com.training.springbootusecase.repository.ConnectionsRepository;
import com.training.springbootusecase.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AccountService {

	@Autowired
	private AccountRepository repository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ConnectionsRepository connectionsRepository;
	
	public List<Account> getAllAccounts() {
		return repository.findAll();
	}
	public Account getAccountById(long accountId){
		if(repository.existsById(accountId))
			return repository.getById(accountId);
		else{
			log.info("Account doesn't exist");
			throw new NoSuchAccountException("Account cannot be found");
		}
	}
	public User getUserByEmail(String email){
		if(userRepository.existsUserByEmail(email))
			return userRepository.findByEmail(email);
		else{
			log.info("Account doesn't exist");
			throw new NoSuchAccountException("Account cannot be found");
		}
	}
	
	public String fundTransfer(FundTransferDto dto){
		Account fromAccount = getAccountById(dto.getFromAccountId());
		Account toAccount = getAccountById(dto.getToAccountId());
		if(fromAccount.getBalance()>dto.getAmount()){
			fromAccount.setBalance(fromAccount.getBalance()-dto.getAmount());
		}
		else{
			log.info("Insufficient Balance");
			throw new NotSufficientFundException("You have low balance. You can't make this transaction");
		}
		toAccount.setBalance(toAccount.getBalance()+dto.getAmount());
		TransactionHistory history = TransactionHistory.builder()
				.fromAccountId(dto.getFromAccountId())
				.toAccountId(dto.getToAccountId())
				.amountDebited(dto.getAmount())
				.balance(fromAccount.getBalance())
				.time(LocalDateTime.now())
				.build();
		fromAccount.setHistory(history);
		repository.save(fromAccount);
		repository.save(toAccount);
		return "transaction successful";
	}
	public BeneficiaryDto addBeneficiary(RequestDto dto){
		Account account = getAccountById(dto.getAccountId());
		BeneficiaryConnections connections= BeneficiaryConnections.builder()
				.account(account)
				.beneficiaryUserName(dto.getBeneficiaryUserName())
				.beneficiaryPersonAccountId(dto.getBeneficiaryPersonAccountId())				
				.build();
		
		connectionsRepository.save(connections);
		return BeneficiaryDto.builder()
				.accountId(account.getAccountId())
				.beneficiaryId(connections.getBeneficiaryPersonAccountId())
				.build();
	}
	
	public CredentialDto addUser(RegisterDto dto){
		if(userRepository.existsUserByEmail(dto.getEmail()))
			throw new DuplicateUserException("User already registered");
		User user = User.builder()
				.name(dto.getName())
				.email(dto.getEmail())
				.gender(dto.getGender())
				.accountType(dto.getAccountType())
				.password(dto.getPassword())
				.build();
		userRepository.save(user);
		Account account = Account.builder()
				.user(user)
				.balance(dto.getBalance())
				.effectiveDate(LocalDate.now())
				.build();
		repository.save(account);
		return CredentialDto.builder()
				.email(user.getEmail())
				.password(user.getPassword())
				.build();		
		
	}
	public AccountInfoDto login (LoginDto dto){
		User user = getUserByEmail(dto.getEmail());
		Account account = repository.findByUser(user);
		if(user.getPassword().equals(dto.getPassword()))
			return AccountInfoDto.builder()
					.email(user.getEmail())
					.balance(account.getBalance())
					.userName(user.getName())
					.build();
		else{
			log.info("Authentication failed");
			throw new AuthenticationException("Password is wrong");
		}
	}
}
