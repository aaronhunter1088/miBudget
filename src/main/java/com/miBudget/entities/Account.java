package com.miBudget.entities;

import lombok.Data;

import java.io.Serializable;

import javax.persistence.*;

@Data
@Entity
@Table(name="accounts")
public class Account implements Serializable {
	
	public Account() {}
	
	public Account(String accountId, int item__id, double availableBalance, double currentBalance,
				   double limit, String currencyCode, String nameOfAcct, String officialNameOfAcct,
				   String mask, String type, String subtype) {
		this.accountId = accountId;
		this.item__id = item__id;
		this.availableBalance = availableBalance;
		this.currentBalance = currentBalance;
		this.limit = limit;
		this.currencyCode = currencyCode;
		this.accountName = nameOfAcct;
		this.officialName = officialNameOfAcct;
		this.mask = mask;
		this.type = type;
		this.subType = subtype;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id", updatable=false, nullable=false)
	private int id;

	@Id
	@Column(name="account_id")
	private String accountId;
	
	@Column(name="item__id")
	private int item__id;
	
	@Column(name="available_balance", precision=10, scale=2)
	private double availableBalance;
	
	@Column(name="current_balance", precision=10, scale=2)
	private double currentBalance;
	
	@Column(name="_limit", precision=10, scale=2)
	private double limit;
	
	@Column(name="iso_currency_code")
	private String currencyCode;
	
	@Column(name="account_name")
	private String accountName;
	
	@Column(name="official_name")
	private String officialName;
	
	@Column(name="mask")
	private String mask;
	
	@Column(name="_type")
	private String type;
	
	@Column(name="subtype")
	private String subType;

	private static final long serialVersionUID = 1L;

}
