package com.training.springbootusecase.entity;


import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Entity
@Table
@Builder
@Data
@ToString
public class Account {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long accountId;
	@OneToOne(cascade = CascadeType.ALL)
	private User user;
	@OneToOne(cascade = CascadeType.ALL)
	private BeneficiaryConnections connections; 
	private double balance;
	public Account() {
		super();
	}
	public Account(long accountId, User user, BeneficiaryConnections connections, double balance) {
		super();
		this.accountId = accountId;
		this.user = user;
		this.connections = connections;
		this.balance = balance;
	}
	

}
