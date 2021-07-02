package com.training.springbootusecase.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.training.springbootusecase.dto.AccountInfoDto;
import com.training.springbootusecase.dto.BeneficiaryDto;
import com.training.springbootusecase.dto.CredentialDto;
import com.training.springbootusecase.dto.RequestDto;
import com.training.springbootusecase.entity.Account;
import com.training.springbootusecase.entity.BeneficiaryConnections;
import com.training.springbootusecase.entity.User;
import com.training.springbootusecase.exceptions.AuthenticationException;
import com.training.springbootusecase.exceptions.NoSuchAccountException;
import com.training.springbootusecase.exceptions.NotSufficientFundException;
import com.training.springbootusecase.repository.AccountRepository;
import com.training.springbootusecase.repository.BeneficiaryConnectionsRepository;
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
	private BeneficiaryConnectionsRepository connectionsRepository;
	
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
		return userRepository.findByEmail(email);
	}
	
	public String fundTransfer(long accountId, double amount){
		Account fromAccount = getAccountById(accountId);
		if(fromAccount.getBalance()>amount){
			fromAccount.setBalance(fromAccount.getBalance()-amount);
		}
		else{
			log.info("Insufficient Balance");
			throw new NotSufficientFundException("You have low balance. You can't make this transaction");
		}
		long toAccountId = fromAccount.getConnections().getBeneficiaryPersonAccountId();
		Account toAccount = getAccountById(toAccountId);
		toAccount.setBalance(toAccount.getBalance()+amount);
		repository.save(fromAccount);
		repository.save(toAccount);
		return "transaction successful";
	}
	public BeneficiaryDto addBeneficiary(RequestDto dto){
		Account account = getAccountById(dto.getAccountId());
		if(account.equals(null))
			throw new NoSuchAccountException("Account cannot be found");
		BeneficiaryConnections connections= BeneficiaryConnections.builder()
				.account(account)
				.beneficiaryUserName(dto.getBeneficiaryUserName())
				.beneficiaryPersonAccountId(dto.getBeneficiaryPersonAccountId())				
				.build();
		
		account.setConnections(connections);
		repository.save(account);
		return BeneficiaryDto.builder()
				.accountId(account.getAccountId())
				.beneficiaryId(connections.getBeneficiaryPersonAccountId())
				.build();
	}
	/*public Account removeBeneficiary(long accountId){
		Account account = getAccountById(accountId);
		account.setConnections(null);
		repository.save(account);
		return account;
	}*/
	public CredentialDto addUser(User user, double balance){
		Account account = Account.builder()
				.user(user)
				.balance(balance)
				.build();
		repository.save(account);
		userRepository.save(user);
		return CredentialDto.builder()
				.email(user.getEmail())
				.password(user.getPassword())
				.build();		
		
	}
	public AccountInfoDto login (String email, String password){
		User user = getUserByEmail(email);
		Account account = repository.findByUser(user);
		if(user.getPassword().equals(password))
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
