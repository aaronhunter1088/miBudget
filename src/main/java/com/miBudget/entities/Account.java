package com.miBudget.entities;

import lombok.Data;

import java.io.Serializable;

import javax.persistence.*;

@Data
@Entity
@Table(name="accounts")
public class Account {

	@Id
	@SequenceGenerator(name="accounts_sequence", sequenceName="accounts_sequence", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="accounts_sequence")
	private Long id;
	private String accountId;
	private Long itemId;
	@Column(name="availableBalance", precision=10, scale=2)
	private double availableBalance;
	@Column(name="currentBalance", precision=10, scale=2)
	private double currentBalance;
	@Column(name="_limit", precision=10, scale=2)
	private double _limit;
	private String currencyCode;
	private String accountName;
	private String officialName;
	private String mask;
	@Column(name="_type")
	private String _type;
	private String subType;
	private Long userId;

	public Account() {}
	
	public Account(String accountId, long itemId, double availableBalance, double currentBalance,
				   double _limit, String currencyCode, String nameOfAcct, String officialNameOfAcct,
				   String mask, String _type, String subtype) {
		this.accountId = accountId;
		this.itemId = itemId;
		this.availableBalance = availableBalance;
		this.currentBalance = currentBalance;
		this._limit = _limit;
		this.currencyCode = currencyCode;
		this.accountName = nameOfAcct;
		this.officialName = officialNameOfAcct;
		this.mask = mask;
		this._type = _type;
		this.subType = subtype;
	}
}
